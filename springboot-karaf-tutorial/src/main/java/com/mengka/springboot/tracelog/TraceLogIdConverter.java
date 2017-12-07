//package com.mengka.springboot.tracelog;
//
//import ch.qos.logback.classic.pattern.ClassicConverter;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA
// * User: huangyy
// * Date: 2016/11/26
// * Time: 14:00
// */
//public class TraceLogIdConverter extends ClassicConverter {
//    public TraceLogIdConverter() {
//    }
//
//    public String convert(ILoggingEvent iLoggingEvent) {
//        Map map = iLoggingEvent.getMDCPropertyMap();
//        return (String)map.get("traceLogId");
//    }
//}
