package com.divya.sprxs.api;

import com.divya.sprxs.model.ApproveRejectCollaboratorRequestsRequest;
import com.divya.sprxs.model.ApproveRejectCollaboratorRequestsResponse;
import com.divya.sprxs.model.CancelMilestonesRequest;
import com.divya.sprxs.model.CancelMilestonesResponse;
import com.divya.sprxs.model.CollaboratorRequestsRequest;
import com.divya.sprxs.model.CollaboratorRequestsResponse;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneRequest;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneResponse;
import com.divya.sprxs.model.ContactUsRequest;
import com.divya.sprxs.model.ContactUsResponse;
import com.divya.sprxs.model.CreateIdeasRequest;
import com.divya.sprxs.model.CreateIdeasResponse;
import com.divya.sprxs.model.CreateMilestonesRequest;
import com.divya.sprxs.model.CreateMilestonesResponse;
import com.divya.sprxs.model.CreateProfileRequest;
import com.divya.sprxs.model.CreateProfileResponse;
import com.divya.sprxs.model.EditIdeaRequest;
import com.divya.sprxs.model.EditIdeaResponse;
import com.divya.sprxs.model.EditMilestonesRequest;
import com.divya.sprxs.model.EditMilestonesResponse;
import com.divya.sprxs.model.IdeaFilesRequest;
import com.divya.sprxs.model.IdeaFilesResponse;
import com.divya.sprxs.model.InviteToCollaborateRequest;
import com.divya.sprxs.model.InviteToCollaborateResponse;
import com.divya.sprxs.model.ListIdeaForCollaborationRequest;
import com.divya.sprxs.model.ListIdeaForCollaborationResponse;
import com.divya.sprxs.model.LodgeConsensusRequest;
import com.divya.sprxs.model.LodgeConsensusResponse;
import com.divya.sprxs.model.LoginRequest;
import com.divya.sprxs.model.LoginResponse;
import com.divya.sprxs.model.MarketPlaceResponse;
import com.divya.sprxs.model.MyCollaborationsResponse;
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
import com.divya.sprxs.model.ShowAvailableTokensToOfferRequest;
import com.divya.sprxs.model.ShowAvailableTokensToOfferResponse;
import com.divya.sprxs.model.ShowEquityForIdeaRequest;
import com.divya.sprxs.model.ShowEquityForIdeaResponse;
import com.divya.sprxs.model.ViewEventsRequest;
import com.divya.sprxs.model.ViewEventsResponse;
import com.divya.sprxs.model.ViewMilestonesRequest;
import com.divya.sprxs.model.ViewMilestonesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @GET("/refreshToken")
    Call<RefreshTokenResponse> refreshToken(
            @Header("Authorization") String token
    );

    @POST("/createProfile_v2")
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

    @POST("/collaborations/invitation")
    Call<InviteToCollaborateResponse> inviteToCollaborate(
            @Header("Authorization") String token,
            @Body InviteToCollaborateRequest inviteToCollaborateRequest
    );

    @POST("/contactus")
    Call<ContactUsResponse> contactUs(
            @Body ContactUsRequest contactUsRequest
    );

    @POST("/showEquityForIdea")
    Call<List<ShowEquityForIdeaResponse>> collaborators(
            @Header("Authorization") String token,
            @Body ShowEquityForIdeaRequest showEquityForIdeaRequest
            );

    @POST("/showAvailableTokensToOffer_IOS")
    Call<ShowAvailableTokensToOfferResponse> availableTokens(
            @Header("Authorization") String token,
            @Body ShowAvailableTokensToOfferRequest showAvailableTokensToOfferRequest
    );

    @POST("/getAllIdeaFiles")
    Call<List<IdeaFilesResponse>> ideaFiles(
            @Header("Authorization") String token,
            @Body IdeaFilesRequest ideaFilesRequest
    );

    //    @GET("/collaborations/invitation/{id")
    //    Call<List<CollaboratorRequestsResponse>> collaboratorRequest(
    //            @Header("Authorization") String token,
    //            @Path("id") Long id,
    //            @Query("pending") String pendingText
    //
    //    );

    @POST("/getCollaboratorsByProfile_android")
    Call<List<CollaboratorRequestsResponse>> getCollaboratorsByProfile(
            @Header("Authorization") String token,
            @Body CollaboratorRequestsRequest collaboratorRequestsRequest
    );

    @POST("/approveCollaborator")
    Call<ApproveRejectCollaboratorRequestsResponse> approveCollaborator(
            @Header("Authorization") String token,
            @Body ApproveRejectCollaboratorRequestsRequest approveRejectCollaboratorRequestsRequest
    );

    @GET("/collaborations/{id}")
    Call<MyCollaborationsResponse> myCollaborations(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    @POST("/createMilestone_v2")
    Call<CreateMilestonesResponse> createMilestones(
            @Header("Authorization") String token,
            @Body CreateMilestonesRequest createMilestoneRequest
    );

    @POST("/viewMilestone")
    Call<List<ViewMilestonesResponse>> viewMilestones(
            @Header("Authorization") String token,
            @Body ViewMilestonesRequest viewMilestonesRequest
    );

    @POST("/getCollaboratorsByIdeaForMilestone")
    Call<List<CollaboratorsByIdeaForMilestoneResponse>> getCollaboratorsByIdeaForMilestone(
            @Header("Authorization") String token,
            @Body CollaboratorsByIdeaForMilestoneRequest collaboratorsByIdeaForMilestoneRequest
    );

    @POST("/editMilestone_v2")
    Call<EditMilestonesResponse> editMilestones(
            @Header("Authorization") String token,
            @Body EditMilestonesRequest editMilestonesRequest
    );

    @POST("/cancelMilestone")
    Call<CancelMilestonesResponse> cancelMilestones(
            @Header("Authorization") String token,
            @Body CancelMilestonesRequest cancelMilestonesRequest
    );

    @POST("/lodgeConsensus")
    Call<LodgeConsensusResponse> lodgeConsensus(
            @Header("Authorization") String token,
            @Body LodgeConsensusRequest lodgeConsensusRequest
    );

}