package com.bulletinboard;

import java.util.Scanner;

import com.bulletinboard.BulletinBoardGrpc.BulletinBoardBlockingStub;
import com.bulletinboard.Post;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BulletinBoardClient {
	
	static int port = 6050;
	static BulletinBoardBlockingStub stub;

	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
		stub = BulletinBoardGrpc.newBlockingStub(channel);
		
		Scanner sc = new Scanner(System.in);
		
		// Accept input loop
		while(true) {
			System.out.print("> ");
			String[] input = sc.nextLine().split("\"");

			//split by quotes so there will be an extra space in front if its 1 of these 3
			//other wise exit and list will be fine
			if(input[0].equals("post ") ){ input[0] = "post"; }
			if(input[0].equals("delete ") ){ input[0] = "delete"; }
			if(input[0].equals("get ") ){ input[0] = "get"; }


			if(input[0].equals("exit"))
				break;
			
			//if the input is for a post the parameters will be 1 and 3
			//otherwise if the command is get or delete it will just be 1 and 2
			if(input[0].equals("post")) {
				addPost(input[1], input[3]);
			}
			
			if(input[0].equals("list")) {
				listPosts();
			}
			
			if(input[0].equals("get")) {
				getPost(input[1]);
			}
			
			if(input[0].equals("delete")) {
				deletePost(input[1]);
			}
		}
		
		sc.close();
	}
	
	public static void addPost(String title, String body) {
		addPostRequest req = addPostRequest.newBuilder().setTitle(title).setBody(body).build();
		
		addPostResponse res = stub.addPost(req);
		
		System.out.println(res.getResponse());
	}
	
	public static void listPosts() {
		getPostsRequest req = getPostsRequest.newBuilder().build();
		getPostsResponse res = stub.getPosts(req);
		
		int length = res.getPostsCount();
		
		if(length == 0)
			System.out.println("There are no posts available");
		else {
			System.out.println("Posts: ");
			for(int i = 0; i < length; i++) {
				Post post = res.getPosts(i);
				String title = post.getTitle();
				String body = post.getBody();
				System.out.println("\t" + (i+1) + ". " + title + " : " + body);
			}
		}
	}
	
	public static void getPost(String title) {
		getPostRequest req = getPostRequest.newBuilder().setTitle(title).build();
		getPostResponse res = stub.getPost(req);
		
		boolean exists = res.getExists();
		if(exists)
			System.out.println(res.getPost().getBody());
		else
			System.out.println("No post with title \"" + title + "\" found");
	}
	
	public static void deletePost(String title) {
		deletePostRequest req = deletePostRequest.newBuilder().setTitle(title).build();
		deletePostResponse res = stub.deletePost(req);
		
		if(res.getResult())
			System.out.println("Succesfully deleted post with title \"" + title + "\"");
		else
			System.out.println("Unable to find post with title \"" + title + "\"");
	}

}
