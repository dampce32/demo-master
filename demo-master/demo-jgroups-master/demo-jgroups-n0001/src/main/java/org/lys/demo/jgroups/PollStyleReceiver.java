package org.lys.demo.jgroups;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.util.Util;

public class PollStyleReceiver implements Runnable {
	private JChannel channel;
	private Map<String, String> state = new HashMap<String, String>();
	private String properties = "UDP(mcast_addr=228.1.2.3;mcast_port=45566;ip_ttl=32):"
			+ "PING(timeout=3000;num_initial_members=6):" + "FD(timeout=5000):" + "VERIFY_SUSPECT(timeout=1500):"
			+ "pbcast.NAKACK(gc_lag=10;retransmit_timeout=3000):" + "UNICAST(timeout=300,600,1200):" + "FRAG:"
			+ "pbcast.GMS(join_timeout=5000;shun=false;print_local_addr=true):" + "pbcast.STATE_TRANSFER:"
			+ "pbcast.FLUSH";

	public void start() throws Exception {
		channel = new JChannel(properties);
		channel.connect("PollStyleReceiver");
//		channel.setOpt(Channel.BLOCK, Boolean.TRUE);
		channel.getState(null, 10000);
		new Thread(this).start();
		sendMessage();
		channel.close();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void run() {
		while (true) {
			try {
				Object obj = channel.receive(0);
//				Object obj = channel.receive(0);
				if (obj instanceof Message) {
					System.out.println("received a regular message: " + (Message) obj);
					String s = (String) ((Message) obj).getObject();
					String key = s.substring(0, s.indexOf("="));
					String value = s.substring(s.indexOf("=") + 1);
					state.put(key, value);
				} else if (obj instanceof View) {
					System.out.println("received a View message: " + (View) obj);
				} else if (obj instanceof BlockEvent) {
					System.out.println("received a BlockEvent message: " + (BlockEvent) obj);
					channel.blockOk();
				} else if (obj instanceof UnblockEvent) {
					System.out.println("received a UnblockEvent message: " + (UnblockEvent) obj);
				} else if (obj instanceof GetStateEvent) {
					System.out.println("received a GetStateEvent message: " + (GetStateEvent) obj);
					channel.returnState(Util.objectToByteBuffer(copyState(state)));
				} else if (obj instanceof SetStateEvent) {
					System.out.println("received a SetStateEvent message: " + (SetStateEvent) obj);
					this.state = (Map<String, String>) Util.objectFromByteBuffer(((SetStateEvent) obj).getArg());
					System.out.println("current state: " + printState(this.state));
				} else {
					System.out.println(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void sendMessage() throws Exception {
		boolean succeed = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.print("> ");
				System.out.flush();
				String line = br.readLine();
				if (line != null && line.equals("exit")) {
					break;
				} else if (line.indexOf("=") > 0 || line.indexOf("=") == line.length() - 1) {
					Message msg = new Message(null, null, line);
					channel.send(msg);
				} else {
					System.out.println("invalid input: " + line);
				}
			}
			succeed = true;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					if (succeed) {
						throw e;
					}
				}
			}
		}
	}

	private Map<String, String> copyState(Map<String, String> s) {
		Map<String, String> m = new HashMap<String, String>();
		for (String key : s.keySet()) {
			m.put(key, s.get(key));
		}
		return m;
	}

	private String printState(Map<String, String> s) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Iterator<String> iter = s.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			sb.append(key).append("=");
			sb.append(s.get(key));
			if (iter.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static void main(String args[]) throws Exception {
		new PollStyleReceiver().start();
	}
}
