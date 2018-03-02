package cn.honry.statistics.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 朱振坤
 */
@Configuration
public class ESUtils {

    public static final String ESAGGPAGE = "esAggPage";
    @Value("${es.host1}")
    private String esHost1;

    @Value("${es.port1}")
    private String esPort1;

    @Value("${es.host2}")
    private String esHost2;

    @Value("${es.port2}")
    private String esPort2;

    @Value("${es.clusterName}")
    private String ES_CLUSTERNAME;

    private static Client client;

    @Bean(name = "client")
    public synchronized Client getESClient() {
        try {
            if (client == null) {
                Settings settings = Settings.settingsBuilder().put("cluster.name", ES_CLUSTERNAME).build();
                client = TransportClient.builder().settings(settings).build()
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost1), Integer.valueOf(esPort1)))
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost2), Integer.valueOf(esPort2)));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    @PreDestroy
    public void closeClient() {
        if (client != null) {
            client.close();
        }
    }
}
