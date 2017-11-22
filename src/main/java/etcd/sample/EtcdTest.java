package etcd.sample;

import mousio.client.retry.RetryWithTimeout;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdVersionResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class EtcdTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtcdTest.class);

    private EtcdClient client;

    @Before
    public void init() {
        final String server = "http://192.168.209.128:2379";
        client = new EtcdClient(URI.create(server));
        client.setRetryHandler(new RetryWithTimeout(200, 20000));
    }

    @Test
    public void version() {
        EtcdVersionResponse response = client.version();
        printLog(response);
    }

    @Test
    public void put() throws IOException, EtcdException, EtcdAuthenticationException, TimeoutException {
        for (int i = 1; i <= 100; i++) {
            EtcdKeysResponse response = client.put("name" + i, UUID.randomUUID().toString()).send().get();
            printLog(response);
        }
    }

    @Test
    public void get() throws IOException, EtcdException, EtcdAuthenticationException, TimeoutException {
        EtcdKeysResponse response = client.get("name").send().get();
        printLog(response);
    }

    @Test
    public void delete() throws IOException, EtcdException, EtcdAuthenticationException, TimeoutException {
        EtcdKeysResponse response = client.delete("name").send().get();
        printLog(response);
    }

    @Test
    public void promise() throws IOException, EtcdException, EtcdAuthenticationException, TimeoutException {
        EtcdResponsePromise<EtcdKeysResponse> promise = client.put("key", "helloworld").send();
        // on change
        promise.addListener((response) -> {
            try {
                printLog(response.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.in.read();
    }

    @After
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printLog(EtcdKeysResponse response) {
        LOGGER.info("Response : {} ", response.toString());
    }

    private void printLog(EtcdVersionResponse response) {
        LOGGER.info("Server : {}", response.server);
        LOGGER.info("Cluster : {}", response.cluster);
    }
}