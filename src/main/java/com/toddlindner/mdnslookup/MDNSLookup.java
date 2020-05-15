package com.toddlindner.mdnslookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MDNSLookup {

	public static void main(String[] args) throws Exception {
		System.out.println(new MDNSLookup().all(Integer.parseInt(args[0])));
	}

	private String all(int to) {
		return IntStream.range(1, to).boxed()
			.map(i -> "10.0.1." + i)
			.map(this::ipToHost)
			.filter(Objects::nonNull)
			.collect(Collectors.joining("\n"));
	}

	private String ipToHost(String ip) {
		try {
			var cmd = "dig +short +tries=2 +timeout=3 -x " + ip + " @224.0.0.251 -p 5353";
			var p = new ProcessBuilder(cmd.split(" ")).start();
			var s = toString(p.getInputStream())
				.replaceAll("\n", "")
				.replaceAll("\r", "")
				.trim();
			p.waitFor(1200, TimeUnit.MILLISECONDS);

			System.err.println(ip + " = [" + s + "]");

			if (s.length() == 0 || s.contains("no servers could")) {
				return null;
			}

			if (s.endsWith(".")) {
				s = s.substring(0, s.length() - 1);
			}
			if (s.endsWith(".local")) {
				s = s.substring(0, s.length() - 6);
			}

			return ip + "    " + s;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toString(InputStream is) throws IOException {
		try (var r = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = r.readLine()) != null) {
				return line; // only want the first line of the result
			}
			return "";
		}
	}
}
