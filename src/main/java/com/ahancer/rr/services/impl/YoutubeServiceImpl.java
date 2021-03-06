package com.ahancer.rr.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.MediaDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Post;
import com.ahancer.rr.response.AuthenticationResponse;
import com.ahancer.rr.response.OAuthenticationResponse;
import com.ahancer.rr.response.YouTubeProfileResponse;
import com.ahancer.rr.services.AuthenticationService;
import com.ahancer.rr.services.YoutubeService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

@Component
@Transactional(rollbackFor=Exception.class)
public class YoutubeServiceImpl implements YoutubeService {
	@Value("${youtube.appKey}")
	private String appKey;
	@Value("${youtube.appSecret}")
	private String appSecret;
	@Value("${youtube.apiKey}")
	private String apiKey;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private MediaDao mediaDao;
	private GoogleAuthorizationCodeFlow authorizationCodeFlow;
	@PostConstruct
	public void init() throws GeneralSecurityException, IOException {
		authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				appKey,
				appSecret, 
				Collections.singleton(YouTubeScopes.YOUTUBE))
				.build();
	}
	
	public String getAccessToken(String authorizationCode, String redirectUri) throws Exception {
		return authorizationCodeFlow.newTokenRequest(authorizationCode).setRedirectUri(redirectUri).execute().getAccessToken();
	}
	
	public YouTube getInstance(String accessToken) throws Exception  {
		Credential credential = new GoogleCredential().setAccessToken(accessToken);
		return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
	}
	
	public Post getPostInfo(String postId) throws Exception{
		Credential credential = new GoogleCredential();
		YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("Reachrabbit-Server/1.05R").build();
		YouTube.Videos.List videoList = youtube.videos().list("statistics");
		videoList.setKey(apiKey).setId(postId);
		
		Video video = videoList.execute().getItems().get(0);
		Long likes = 0L;
		Long comments = 0L;
		Long views = 0L;
		
		if(null != video && null != video.getStatistics()){
			if(null != video.getStatistics().getLikeCount()){
				likes = video.getStatistics().getLikeCount().longValue();
			}
			if(null != video.getStatistics().getCommentCount()){
				comments = video.getStatistics().getCommentCount().longValue();
			}
			if(null != video.getStatistics().getViewCount()){
				views = video.getStatistics().getViewCount().longValue();
			}
		}
		
		Post post = new Post();
		post.setLikeCount(likes);
		post.setCommentCount(comments);
		post.setViewCount(views);
		post.setShareCount(0L);
		post.setSocialPostId(postId);
		return post;
		
	}
	
	public YouTubeProfileResponse getVideoFeed(String channelId) throws Exception {
		Credential credential = new GoogleCredential();
		YouTubeProfileResponse ytres = new YouTubeProfileResponse();
		YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName("Reachrabbit-Server/1.05R").build();
		
		YouTube.Channels.List chanlist = youtube.channels().list("snippet,contentDetails,statistics");
		chanlist.setKey(apiKey).setId(channelId);
		ChannelListResponse chanResult = chanlist.execute();
		if(chanResult.getItems().size() == 0){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.profile.youtube.not.public");
		}
		Channel chan = chanResult.getItems().get(0);
		
		List<PlaylistItem> pl = new ArrayList<>();
		String playlistId = chanResult.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads();
		YouTube.PlaylistItems.List ypllist = youtube.playlistItems().list("snippet");
		ypllist.setPart("snippet,contentDetails");
		ypllist.setKey(apiKey).setPlaylistId(playlistId);

		PlaylistItemListResponse result_pitem = ypllist.execute();
		pl.addAll(result_pitem.getItems());
		
		HashSet<String> videoIds = new HashSet<String>();
		
		for(PlaylistItem pitem: pl){
			String videoId = pitem.getContentDetails().getVideoId();
			videoIds.add(videoId);
		}
		
		String videoQuery =  String.join(",", videoIds);
		YouTube.Videos.List yvlist = youtube.videos().list("snippet,statistics");
		yvlist.setKey(apiKey).setId(videoQuery);
		VideoListResponse result = yvlist.execute();
		
		ytres.setVideos(result.getItems());
		ytres.setChannel(chan);
		
		return ytres;
		
	}
	
	public OAuthenticationResponse authentication(String accessToken, String ip) throws Exception {
		YouTube youtube = getInstance(accessToken);
		YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
		channelRequest.setMine(true);
		channelRequest.setPart("snippet,statistics");
		
		ChannelListResponse channelResult = channelRequest.execute();
		
		List<Channel> channelsList = channelResult.getItems();
		
		if(channelsList == null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "error.youtube.no.channel");
		}
		
		Channel channel = channelsList.get(0);
		List<OAuthenticationResponse.Page> pages = new ArrayList<OAuthenticationResponse.Page>();
		pages.add(new OAuthenticationResponse.Page(channel.getId(), channel.getSnippet().getTitle(), null, channel.getStatistics().getSubscriberCount(), false));
		
		AuthenticationResponse auth = authenticationService.influencerAuthentication(channel.getId(), "google", ip);
		
		if(auth == null) {
			OAuthenticationResponse oauth = new OAuthenticationResponse();
			oauth.setName(channel.getSnippet().getTitle());
			oauth.setId(channel.getId());
			oauth.setMedia(mediaDao.findByMediaId("google"));
			oauth.setProfilePicture(channel.getSnippet().getThumbnails().getHigh().getUrl());
			oauth.setPages(pages);
			return oauth;
		} else {
			return new OAuthenticationResponse(auth.getToken());
		}
	}
}
	
