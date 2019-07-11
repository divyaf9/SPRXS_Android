package com.divya.sprxs.api;

import com.divya.sprxs.model.CreateIdeasRequest;
import com.divya.sprxs.model.CreateIdeasResponse;
import com.divya.sprxs.model.CreateProfileRequest;
import com.divya.sprxs.model.CreateProfileResponse;
import com.divya.sprxs.model.EditIdeaRequest;
import com.divya.sprxs.model.EditIdeaResponse;
import com.divya.sprxs.model.ListIdeaForCollaborationRequest;
import com.divya.sprxs.model.ListIdeaForCollaborationResponse;
import com.divya.sprxs.model.LoginRequest;
import com.divya.sprxs.model.LoginResponse;
import com.divya.sprxs.model.MarketPlaceResponse;
import com.divya.sprxs.model.MyIdeasRequest;
import com.divya.sprxs.model.MyIdeasResponse;
import com.divya.sprxs.model.MyIdeasSummaryRequest;
import com.divya.sprxs.model.MyIdeasSummaryResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.RequestWorkOnIdeaRequest;
import com.divya.sprxs.model.RequestWorkOnIdeaResponse;
import com.divya.sprxs.model.ResetPasswordRequest;
import com.divya.sprxs.model.ResetPasswordResponse;
import com.divya.sprxs.model.SearchIdeaRequest;
import com.divya.sprxs.model.SearchIdeaResponse;
import com.divya.sprxs.model.ViewEventsRequest;
import com.divya.sprxs.model.ViewEventsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @GET("/refreshToken")
    Call<RefreshTokenResponse> refreshToken(
            @Header("Authorization") String token
    );

    @POST("/createProfile")
    Call<CreateProfileResponse> userSignup(
            @Body CreateProfileRequest createProfileRequest
    );

    @POST("/loginProfile_V2")
    Call<LoginResponse> userLogin(
            @Body LoginRequest loginRequest
    );

    @POST("/resetPassword")
    Call<ResetPasswordResponse> resetPassword(
            @Body ResetPasswordRequest resetPasswordRequest
    );

    @POST("/createIdea_v2")
    Call<CreateIdeasResponse> createIdea(
            @Header("Authorization") String token,
            @Body CreateIdeasRequest createIdeasRequest
    );

    @POST("/editIdea_v2")
    Call<EditIdeaResponse> editIdea(
            @Header("Authorization") String token,
            @Body EditIdeaRequest editIdeaRequest
    );

    @POST("/myIdeasSummary")
    Call<List<MyIdeasSummaryResponse>> ideasSummary(
            @Header("Authorization") String token,
            @Body MyIdeasSummaryRequest myIdeasSummaryRequest
    );

    @POST("/myIdeas")
    Call<List<MyIdeasResponse>> myIdeas(
            @Header("Authorization") String token,
            @Body MyIdeasRequest myIdeasRequest
    );

    @POST("/viewEvent")
    Call<List<ViewEventsResponse>> viewEvent(
            @Header("Authorization") String token,
            @Body ViewEventsRequest viewEventsRequest
    );

    @POST("/listIdeaForCollaboration")
    Call<ListIdeaForCollaborationResponse> publishIdea(
            @Header("Authorization") String token,
            @Body ListIdeaForCollaborationRequest listIdeaForCollaborationRequest
    );

    @POST("/searchIdea")
    Call<List<SearchIdeaResponse>> searchIdea(
            @Header("Authorization") String token,
            @Body SearchIdeaRequest searchIdeasRequest
    );

    @POST("/requestWorkOnIdea")
    Call<RequestWorkOnIdeaResponse> requestWorkOnIdea(
            @Header("Authorization") String token,
            @Body RequestWorkOnIdeaRequest requestWorkOnIdeaRequest
    );

    @GET("/collaborator")
    Call<List<MarketPlaceResponse>> marketPlace(
            @Header("Authorization") String token);
}