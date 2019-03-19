package com.bulletinboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bulletinboard.getPostsResponse.Builder;
import com.bulletinboard.Post;

import io.grpc.stub.StreamObserver;

public class BulletinBoardService extends BulletinBoardGrpc.BulletinBoardImplBase {
	
	// This hash table stores all the posts. The key is title and the value is post contents
	HashMap<String, String> POSTS = new HashMap<String, String>();
	
	public void addPost(addPostRequest request, StreamObserver<addPostResponse> responseObserver) {
		String title = new String(request.getTitle());
		String body = new String(request.getBody());
		System.out.println("Posting post with title \"" + title + "\" and body \"" + body + "\"");
		POSTS.put(title, body);
				
		addPostResponse res = addPostResponse.newBuilder().setResponse("success").build();
		responseObserver.onNext(res);
		responseObserver.onCompleted();
	}
	
	public void getPosts(getPostsRequest request, StreamObserver<getPostsResponse> responseObserver) {
		Builder builder = getPostsResponse.newBuilder();
						
		for(String title: POSTS.keySet()) {
			Post post = Post.newBuilder().setTitle(title).setBody(POSTS.get(title)).build();
			builder.addPosts(post);
		}
		
		System.out.println("Sending list of all posts");
		
		getPostsResponse res = builder.build();
		responseObserver.onNext(res);
		responseObserver.onCompleted();
		
	}
	
	public void getPost(getPostRequest request, StreamObserver<getPostResponse> responseObserver) {
		getPostResponse res;
		String title = request.getTitle();
		
		if(POSTS.containsKey(title)) {
			Post post = Post.newBuilder().setTitle(title).setBody(POSTS.get(title)).build();
			res = getPostResponse.newBuilder().setExists(true).setPost(post).build();
			System.out.println("Sending post with title \"" + title + "\"");
		}
		else {
			res = getPostResponse.newBuilder().setExists(false).build();
			System.out.println("Unable to get post with title \"" + title + "\"");
		}
		
		responseObserver.onNext(res);
		responseObserver.onCompleted();
	}
	
	public void deletePost(deletePostRequest request, StreamObserver<deletePostResponse> responseObserver) {
		deletePostResponse res;
		String title = request.getTitle();
		
		if(POSTS.containsKey(title)) {
			POSTS.remove(title);
			res = deletePostResponse.newBuilder().setResult(true).build();
			System.out.println("Deleted post with title \"" + title + "\"");
		}
		else {
			res = deletePostResponse.newBuilder().setResult(false).build();
			System.out.println("Unable to delete post with title \"" + title + "\" Cound not find post");
		}
		
		responseObserver.onNext(res);
		responseObserver.onCompleted();
	}
}
