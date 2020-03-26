package com.example.passenger.utils;

import com.example.passenger.entity.SnmpModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SnmpDao {
    private static final Logger logger = LoggerFactory.getLogger(SnmpDao.class);

    /**
     * 获取指定OID对应的table值
     *
     * @param oid
     * @param snmpModel
     * @return
     */
    public List<String> walkByTable(String oid, SnmpModel snmpModel) {
        //initSnmp(snmpModel);
        Snmp snmp = null;
        PDU pdu;
        CommunityTarget target;
        List<String> result = new ArrayList<String>();
        try {
            DefaultUdpTransportMapping dm = new DefaultUdpTransportMapping();
            //dm.setSocketTimeout(5000);
            snmp = new Snmp(dm);
            snmp.listen();
            target = new CommunityTarget();
            target.setCommunity(new OctetString(snmpModel.getCommunityName()));
            target.setVersion(snmpModel.getVersion());
            target.setAddress(new UdpAddress(snmpModel.getHostIp() + "/" + snmpModel.getPort()));
            target.setTimeout(1000);
            target.setRetries(1);

            TableUtils tutils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
            OID[] columns = new OID[1];
            columns[0] = new VariableBinding(new OID(oid)).getOid();
            List<TableEvent> list = (List<TableEvent>) tutils.getTable(target, columns, null, null);
            for (TableEvent e : list) {
                VariableBinding[] vb = e.getColumns();
                if (null == vb) continue;
                result.add(vb[0].getVariable().toString());
            }
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return result;
    }

    public String getSnmpGet(String oid, SnmpModel snmpModel) {
        Snmp snmp = null;
        CommunityTarget target;
        String Data;
        try {
            DefaultUdpTransportMapping dm = new DefaultUdpTransportMapping();
            //dm.setSocketTimeout(5000);
            snmp = new Snmp(dm);
            snmp.listen();
            target = new CommunityTarget();
            target.setCommunity(new OctetString(snmpModel.getCommunityName()));
            target.setVersion(snmpModel.getVersion());
            target.setAddress(new UdpAddress(snmpModel.getHostIp() + "/" + snmpModel.getPort()));
            target.setTimeout(1000);
            target.setRetries(1);
            PDU pdus = new PDU();
            if (snmpModel.getAsync() == 1) {
                pdus.add(new VariableBinding(new OID(oid), new OctetString(snmpModel.getParameter())));
                // 设置请求方式
                pdus.setType(PDU.SET);
            } else {
                pdus.add(new VariableBinding(new OID(oid)));
                // 设置请求方式
                pdus.setType(PDU.GET);
            }
            ResponseEvent event = snmp.send(pdus, target);
            if (null != event) {
                if (null != event && event.getResponse() != null) {
                    Vector<VariableBinding> vector = new Vector<>();
                    //Vector<VariableBinding> vector = (Vector<VariableBinding>) event.getResponse().getVariableBindings();
                    //进行类型检查
                    if (event.getResponse().getVariableBindings() instanceof Vector<?>) {
                        for (Object o : (Vector<?>) event.getResponse().getVariableBindings()) {
                            vector.add(VariableBinding.class.cast(o));
                        }
                    }
                    for (int i = 0; i < vector.size(); i++) {
                        VariableBinding vec = vector.elementAt(i);
                        Data = vec.getVariable().toString();
                        return Data;
                    }
                }
            }
            snmp.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            try {
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
