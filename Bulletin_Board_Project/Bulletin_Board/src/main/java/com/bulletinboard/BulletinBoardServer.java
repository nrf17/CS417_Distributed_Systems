package com.bulletinboard;

import java.io.IOException;
import java.util.HashMap;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BulletinBoardServer {
	
	static int port = 6050;

	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(port).addService(new BulletinBoardService()).build();
		server.start();
		System.out.println("Starting server on port " + port);
		server.awaitTermination();
	}

}
