package io.turntable.projectwebservice.configurations;

import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.exporters.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.TracerSdkFactory;
import io.opentelemetry.sdk.trace.export.SimpleSpansProcessor;
import io.opentelemetry.trace.Tracer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OTConfig {

    public static Tracer observabilityConfig(SpanProcessor spanProcessor){
        TracerSdkFactory tracerFactory = OpenTelemetrySdk.getTracerFactory();
        tracerFactory.addSpanProcessor(spanProcessor);
        return tracerFactory.get("ProjectController");
    }

    public static SpanProcessor spanConfig(){
        return SimpleSpansProcessor.newBuilder(JaegerGrpcSpanExporter.newBuilder()
                .setServiceName("ProjectService-Real")
                .setChannel(ManagedChannelBuilder.forAddress("localhost", 14250)
                        .usePlaintext().build())
                .build()).build();
    }
}
