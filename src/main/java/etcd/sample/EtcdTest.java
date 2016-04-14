package etcd.sample;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mousio.client.retry.RetryWithTimeout;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdLeaderStatsResponse;
import mousio.etcd4j.responses.EtcdLeaderStatsResponse.FollowerInfo;
import mousio.etcd4j.responses.EtcdSelfStatsResponse;
import mousio.etcd4j.responses.EtcdStoreStatsResponse;
import mousio.etcd4j.responses.EtcdVersionResponse;

public class EtcdTest {

	private EtcdClient client;

	@Before
	public void init() {
		final String server = "http://192.168.137.130:2379";
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
		EtcdKeysResponse response = client.put("name", "aaa").send().get();
		printLog(response);
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
	public void stat() {
		EtcdLeaderStatsResponse leaderStatsResponse = client.getLeaderStats();
		System.out.println(leaderStatsResponse.getLeader());
		System.out.println("============================================");

		Map<String, FollowerInfo> followerInfoMap = leaderStatsResponse.getFollowers();
		for (Entry<String, FollowerInfo> entry : followerInfoMap.entrySet()) {
			System.out.println(entry.getKey());
			FollowerInfo followerInfo = entry.getValue();

			System.out.println(followerInfo.getCounts().getSuccess());
			System.out.println(followerInfo.getCounts().getFail());

			System.out.println(followerInfo.getLatency().getAverage());
			System.out.println(followerInfo.getLatency().getCurrent());
			System.out.println(followerInfo.getLatency().getMaximum());
			System.out.println(followerInfo.getLatency().getMinimum());
			System.out.println(followerInfo.getLatency().getStandardDeviation());
		}

		System.out.println("============================================");

		EtcdSelfStatsResponse selfStatsResponse = client.getSelfStats();
		System.out.println(selfStatsResponse.getId());
		System.out.println(selfStatsResponse.getName());
		System.out.println(selfStatsResponse.getRecvAppendRequestCnt());
		System.out.println(selfStatsResponse.getRecvBandwidthRate());
		System.out.println(selfStatsResponse.getRecvPkgRate());
		System.out.println(selfStatsResponse.getSendAppendRequestCnt());
		System.out.println(selfStatsResponse.getState());
		System.out.println(selfStatsResponse.getLeaderInfo().getLeader());
		System.out.println(selfStatsResponse.getLeaderInfo().getUptime());
		System.out.println(selfStatsResponse.getLeaderInfo().getStartTime());
		System.out.println(selfStatsResponse.getStartTime());

		System.out.println("============================================");

		EtcdStoreStatsResponse storeStatsResponse = client.getStoreStats();
		System.out.println(storeStatsResponse.getCompareAndSwapFail());
		System.out.println(storeStatsResponse.getCompareAndSwapSuccess());
		System.out.println(storeStatsResponse.getCreateFail());
		System.out.println(storeStatsResponse.getCreateSuccess());
		System.out.println(storeStatsResponse.getDeleteFail());
		System.out.println(storeStatsResponse.getDeleteSuccess());
		System.out.println(storeStatsResponse.getExpireCount());
		System.out.println(storeStatsResponse.getGetsFail());
		System.out.println(storeStatsResponse.getsSuccess());
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
		System.out.println(response.action);
		System.out.println(response.node.key);
		System.out.println(response.node.value);
		System.out.println(response.node.ttl);
	}

	private void printLog(EtcdVersionResponse response) {
		System.out.println("server : " + response.server);
		System.out.println("cluster : " + response.cluster);
	}
}
