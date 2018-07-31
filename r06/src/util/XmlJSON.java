package util;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

/**
 * 
 * JSON-XML转换工具
 * 
 * @author 
 * @version 1.0
 */
public class XmlJSON {
    private static final String XML = "<?xml version=\"1.0\" encoding=\"GBK\"?><!-- 1..1 --><cisReports receiveTime=\"查询申请时间,格式YYYYMMDD HH24:mm:ss\" queryCount=\"查询请求数量\" queryUserID=\"查询操作员登录名\" subOrgan=\"分支机构名称\" unitName=\"查询单位名称\" batNo=\"查询批次号\"><!-- 以下为每个查询申请的查询结果 1..n --><cisReport isFrozen=\"该客户是否被冻结，true：被冻结，false：未被冻结\" hasSystemError=\"有否系统错误，true：有错误，false：无错误\" refID=\"引用ID,为查询申请条件中的引用ID\" subReportTypesShortCaption=\"报告类型简称\" treatResult=\"对应的收费子报告收费次数,与subReportTypes一一对应,为大于等于0的值的集合,用逗号分隔\" subReportTypes=\"查询的收费子报告ID,多个收费子报告ID用逗号分隔\" queryReasonID=\"查询原因ID，详见数据字典\" buildEndTime=\"报告生成结束时间,格式YYYY-MM-DD HH24:mm:ss\" reportID=\"报告编号\"><!-- 查询条件信息 1..1 --><queryConditions><!-- 1..n --><item><name>查询条件英文名称</name><caption>查询条件中文名称</caption><value>查询条件值</value></item></queryConditions><!-- 个人手机号码核查 0..1 --><mobileCheckInfo treatResult=\"子报告查询状态,1：查得，2：未查得，3：其他原因未查得\" errorMessage=\"treatResult=3时的错误描述信息,treatResult!=3时,该属性的值为空\" treatErrorCode=\"treatResult=3时的错误代码,详见数据字典,treatResult!=3时,该属性不存在\" subReportTypeCost=\"96027\" subReportType=\"13612\"><!-- 0..1 --><item><!-- 具体核查结果，如一致，不一致 --><nameCheckResult>姓名核查结果,有：一致、基本一致、不一致、无法核查 </nameCheckResult><documentNoCheckResult>证件号码核查结果，有：一致、不一致、无法核查 </documentNoCheckResult><phoneCheckResult>手机号码核查结果:一致 </phoneCheckResult><operator>运营商 1：中国电信 2：中国移动 3:中国联通 </operator><areaInfo>手机号码归属地 </areaInfo></item></mobileCheckInfo><!-- 手机号码状态信息 0..1 --><mobileStatusInfo treatResult=\"子报告查询状态,1：查得，2：未查得，3：其他原因未查得\" errorMessage=\"treatResult=3时的错误描述信息,treatResult!=3时,该属性的值为空\" treatErrorCode=\"treatResult=3时的错误代码,详见数据字典,treatResult!=3时,该属性不存在\" subReportTypeCost=\"96027\" subReportType=\"13611\"><item><operator>运营商 1：中国电信 2：中国移动 3:中国联通 </operator><areaInfo>手机号码归属地 </areaInfo><phoneStatus>手机状态 1：正常在用 2: 停机 3：未启用 4：已销号 5-其他 6：预销号(该值可能为空)</phoneStatus><timeLength>手机号码在网时长，如：在网时长不足1个月，1-2个月，3-6个月，7-12个月(该值可能为空) </timeLength><cancelTime>最近一次销号时间,可能为空 格式yyyyMMdd</cancelTime></item></mobileStatusInfo></cisReport></cisReports>";
    private static final String XML1="<cisReports receiveTime=\"查询申请时间,格式YYYYMMDD HH24:mm:ss\" queryCount=\"查询请求数量\" queryUserID=\"查询操作员登录名\" subOrgan=\"分支机构名称\" unitName=\"查询单位名称\" batNo=\"查询批次号\"><cisReport isFrozen=\"该客户是否被冻结，true：被冻结，false：未被冻结\" hasSystemError=\"有否系统错误，true：有错误，false：无错误\" refID=\"引用ID,为查询申请条件中的引用ID\" treatResult=\"对应的收费子报告收费次数,与subReportTypes一一对应,为大于等于0的值的集合,用逗号分隔\" subReportTypes=\"查询的收费子报告ID,多个收费子报告ID用逗号分隔\" queryReasonID=\"查询原因ID，详见数据字典\" buildEndTime=\"报告生成结束时间,格式YYYY-MM-DD HH24:mm:ss\" reportID=\"报告编号\"><queryConditions><item><name>查询条件英文名称</name><caption>查询条件中文名称</caption><value>查询条件值</value></item></queryConditions><telCheckInfo treatResult=\"子报告查询状态,1：查得，2：未查得，3：其他原因未查得\" errorMessage=\"treatResult=3时的错误描述信息,treatResult!=3时,该属性的值为空\" treatErrorCode=\"treatResult=3时的错误代码,详见数据字典,treatResult!=3时,该属性不存在\" subReportTypeCost=\"21603\" subReportType=\"21603\"><resultType> 电话反查结果类型。 1: 固话精确匹配（用户提交查询的电话号码为固定电话，且系统查询到与提交的电话完全匹配的结果）； </resultType><fixedTelMatch1><item><ownerName>机主名称，可能为企业名称，也可能为个人姓名</ownerName><areaCode>行政区划代码</areaCode><areaDesc>行政区划名称</areaDesc><address>联系地址（不含行政区划信息）</address></item></fixedTelMatch1></telCheckInfo><MoblieCheckInfo treatResult=\"子报告查询状态,1：查得，2：未查得，3：其他原因未查得\" errorMessage=\"treatResult=3时的错误描述信息,treatResult!=3时,该属性的值为空\" treatErrorCode=\"treatResult=3时的错误代码,详见数据字典,treatResult!=3时,该属性不存在\" subReportTypeCost=\"21615\" subReportType=\"21615\"><resultType> 电话反查结果类型。反查结果一共分为2种情况，每种情况返回的内容不同。 1: 移动电话精确匹配（用户提交查询的电话号码为移动电话，且系统查询到与提交的电话完全匹配的结果）； 2: 移动电话非精确匹配（用户提交查询的电话号码为移动电话，但系统仅查询到与提交的电话不完全匹配的结果）； </resultType><mobileMatch><item><contactName>此移动电话对应的联系人姓名</contactName><mobileType>移动电话号码类型，如：移动全球通卡</mobileType><mobileAreaDesc>移动电话号码归属地，如：广东深圳</mobileAreaDesc><address>联系地址（含行政区划信息），精确匹配时该字段有值，非精确匹配时该字段值为空</address></item></mobileMatch></MoblieCheckInfo><telCheckInfo treatResult=\"子报告查询状态,1：查得，2：未查得，3：其他原因未查得\" errorMessage=\"treatResult=3时的错误描述信息,treatResult!=3时,该属性的值为空\" treatErrorCode=\"treatResult=3时的错误代码,详见数据字典,treatResult!=3时,该属性不存在\" subReportTypeCost=\"13600\" subReportType=\"13600\"><resultType> 电话反查结果类型。 1: 固话精确匹配（用户提交查询的电话号码为固定电话，且系统查询到与提交的电话完全匹配的结果）； </resultType><fixedTelMatch1><item><ownerName>机主名称，可能为企业名称，也可能为个人姓名</ownerName><areaCode>行政区划代码</areaCode><areaDesc>行政区划名称</areaDesc><address>联系地址（不含行政区划信息）</address></item></fixedTelMatch1></telCheckInfo></cisReport></cisReports>";
	public static String xml2JSON(String xml) {
		return new XMLSerializer().read(xml).toString();
	}

	public static String json2XML(String json) {
		JSONObject jobj = JSONObject.fromObject(json);
		String xml = new XMLSerializer().write(jobj);
		return xml;
	}
	public static void main(String[] args) {
		//System.out.println(xml2JSON(XML1).replace("@", ""));
		System.out.println(xml2JSON(XML1).replace("@", ""));
	}
}
