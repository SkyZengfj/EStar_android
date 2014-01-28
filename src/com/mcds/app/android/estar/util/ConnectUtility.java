package com.mcds.app.android.estar.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

public class ConnectUtility {
	/**
	 * 商品图文详情地址
	 */
	public static final String GIFT_DETAIL_CONTENT_ADDRESS = "http://syx.crc-vip.com/Home/Products/detail/code/"; //E0780.shtml";

	private static final String NAME_SPACE = "http://service.mc.numenzq.com/";

	private static final String SERVICE_ADDRES = "http://incentivetest.dlb666.com/services/onlineService?wsdl";
//	private static final String SERVICE_ADDRES = "http://192.168.88.217:8080/incentive/services/onlineService?wsdl";
//	private static final String SERVICE_ADDRES = "http://192.168.33.32:8989/incentive/services/onlineService?wsdl";

	private static final String INTEGRAL_SERVICE_ADDRES = "http://incentivetest.dlb666.com/services/onlineIntegralService?wsdl";
	private static final String SP_NAME = "zhong";
	private static final String SP_PASSWORD = "guo";

	public static String connect(String methodName, Map<String, Object> params) throws Exception {
		SoapObject request = new SoapObject(NAME_SPACE, methodName);

		if (params != null) {
			JSONObject paramsJo = new JSONObject();

			Set<String> paramsKey = params.keySet();
			for (Iterator<String> i = paramsKey.iterator(); i.hasNext();) {
				String name = i.next();
				Object value = params.get(name);

				paramsJo.put(name, value);
			}

			String paramsValue = paramsJo.toString();
			if (paramsValue.length() > 0) {
				request.addProperty("stringJson", paramsJo.toString());
			}
		}

		Element[] header = new Element[1];
		header[0] = new Element().createElement(NAME_SPACE, "RequestSOAPHeader");
		Element codename = new Element().createElement(NAME_SPACE, "spId");
		codename.addChild(Node.TEXT, SP_NAME);
		header[0].addChild(Node.ELEMENT, codename);
		Element pass = new Element().createElement(NAME_SPACE, "spPassword");
		pass.addChild(Node.TEXT, SP_PASSWORD);
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
//		envelope.bodyOut = request;
		envelope.headerOut = header;
		HttpTransportSE ht = new HttpTransportSE(SERVICE_ADDRES, 10000);
		ht.debug = true;

		String result = "";
		try {
			ht.call(null, envelope);
			if (envelope.getResponse() != null) {
				Object response = envelope.getResponse();
				result = response.toString();
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	public static String connectIntegral(String methodName, Map<String, Object> params) throws Exception {
		SoapObject request = new SoapObject(NAME_SPACE, methodName);

		if (params != null) {
			JSONObject paramsJo = new JSONObject();

			Set<String> paramsKey = params.keySet();
			for (Iterator<String> i = paramsKey.iterator(); i.hasNext();) {
				String name = i.next();
				Object value = params.get(name);

				paramsJo.put(name, value);
			}

			String paramsValue = paramsJo.toString();
			if (paramsValue.length() > 0) {
				request.addProperty("stringJson", paramsJo.toString());
			}
		}

		Element[] header = new Element[1];
		header[0] = new Element().createElement(NAME_SPACE, "RequestSOAPHeader");
		Element codename = new Element().createElement(NAME_SPACE, "spId");
		codename.addChild(Node.TEXT, SP_NAME);
		header[0].addChild(Node.ELEMENT, codename);
		Element pass = new Element().createElement(NAME_SPACE, "spPassword");
		pass.addChild(Node.TEXT, SP_PASSWORD);
		header[0].addChild(Node.ELEMENT, pass);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
//		envelope.bodyOut = request;
		envelope.headerOut = header;
		HttpTransportSE ht = new HttpTransportSE(INTEGRAL_SERVICE_ADDRES, 10000);
		ht.debug = true;

		String result = "";
		try {
			ht.call(null, envelope);
			if (envelope.getResponse() != null) {
				Object response = envelope.getResponse();
				result = response.toString();
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
}
