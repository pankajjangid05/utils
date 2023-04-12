package com.uci.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.inversoft.rest.ClientResponse;
import com.uci.utils.bot.util.BotUtil;

import com.uci.utils.dto.BotServiceResponse2;
import com.uci.utils.dto.BotServiceResponse;
import com.uci.utils.dto.Result;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.Application;
import io.fusionauth.domain.api.ApplicationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.cache.CacheMono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("ALL")
@Service
@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class BotService {

    public WebClient webClient;
    public FusionAuthClient fusionAuthClient;
    private Cache<Object, Object> cache;

    /**
     * Retrieve Bot Node from Starting Message
     *
     * @param startingMessage
     * @return
     */
    public Mono<Result> getBotNodeFromStartingMessage(String startingMessage) {
        String cacheKey = "bot-for-starting-message:" + startingMessage;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? (Result) cache.getIfPresent(key) : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                                .uri(builder -> builder.path("admin/bot/search/internal")
                                        .queryParam("perPage", 5)
                                        .queryParam("page", 1)
                                        .queryParam("match", true)
                                        .queryParam("startingMessage", startingMessage)
                                        .build())
                                .retrieve().bodyToMono(String.class).map(response -> {
                                    if (response != null) {
                                        log.info(response);
                                        try {
                                            ObjectMapper mapper = new ObjectMapper();
                                            BotServiceResponse botServiceResponse = mapper.readValue(response, BotServiceResponse.class);
                                            log.info("Bot Service Response : " + botServiceResponse);
                                            if (botServiceResponse != null && botServiceResponse.getResult() != null && botServiceResponse.getResult().size() > 0) {
                                                return botServiceResponse.getResult().get(0);
                                            }
//									JsonNode root = mapper.readTree(response);
//									if(root.path("result") != null && root.path("result").get(0) != null && !root.path("result").get(0).isEmpty()) {
//										return root.path("result").get(0);
//									}
                                            return new Result();
                                        } catch (JsonProcessingException jsonMappingException) {
                                            return new Result();
                                        }
                                    } else {
                                        return new Result();
                                    }
                                })
                                .doOnError(throwable -> log.info("Error in getting campaign: " + throwable.getMessage()))
                                .onErrorReturn(null)
                )
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }


    /**
     * Retrieve Bot Node from Bot Name
     *
     * @param startingMessage
     * @return
     */
    public Mono<Result> getBotNodeFromName(String botName) {
        String cacheKey = "bot-for-name:" + botName;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? (Result) cache.getIfPresent(key) : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                                .uri(builder -> builder.path("admin/bot/search/internal")
                                        .queryParam("perPage", 5)
                                        .queryParam("page", 1)
                                        .queryParam("match", true)
                                        .queryParam("name", botName)
                                        .build())
                                .retrieve().bodyToMono(String.class).map(response -> {
                                    if (response != null) {
                                        log.info(response);
                                        try {
                                            ObjectMapper mapper = new ObjectMapper();
                                            BotServiceResponse botServiceResponse = mapper.readValue(response, BotServiceResponse.class);
                                            log.info("Bot Service Response : " + botServiceResponse);
                                            if (botServiceResponse != null && botServiceResponse.getResult() != null && botServiceResponse.getResult().size() > 0) {
                                                return botServiceResponse.getResult().get(0);
                                            }
//                                    ObjectMapper mapper = new ObjectMapper();
//                                    JsonNode root = mapper.readTree(response);
//                                    if (root.path("result") != null && root.path("result").get(0) != null && !root.path("result").get(0).isEmpty()) {
//                                        return root.path("result").get(0);
//                                    }
//                                    return new ObjectMapper().createObjectNode();
                                            return new Result();
                                        } catch (JsonProcessingException jsonMappingException) {
                                            return new Result();
                                        }

                                    } else {
//                                return new ObjectMapper().createObjectNode();
                                        return new Result();
                                    }
                                })
                                .doOnError(throwable -> log.info("Error in getting campaign: " + throwable.getMessage()))
                                .onErrorReturn(new Result())
                )
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }

    /**
     * Retrieve Bot Node From its Identifier
     *
     * @param botId - Bot Identifier
     * @return Application
     */
    public Mono<Result> getBotNodeFromId(String botId) {
        String cacheKey = "bot-node-by-id:" + botId;
        return CacheMono.lookup(key -> Mono.justOrEmpty((Result) cache.getIfPresent(cacheKey))
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                        .uri(builder -> builder.path("admin/bot/" + botId).build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(response -> {
                                    if (response != null) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        try {
                                            BotServiceResponse2 botServiceResponse = mapper.readValue(response, BotServiceResponse2.class);
                                            log.info("Bot Service Response : " + botServiceResponse);
                                            if (botServiceResponse != null && botServiceResponse.getResult() != null) {
                                                return botServiceResponse.getResult();
                                            }
//                                            JsonNode root = mapper.readTree(response);
//                                            if (root.path("result") != null && !root.path("result").isEmpty()) {
//                                                return root.path("result");
//                                            }
                                            return null;
                                        } catch (JsonProcessingException e) {
                                            return null;
                                        }
                                    }
                                    return null;
                                }
                        ))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }

    /**
     * Get Adapter id by bot name
     * @param botName
     * @return
     */
//	public Mono<String> getAdapterIdFromBotName(String botName) {
//		String cacheKey = "valid-adpater-from-bot-name: " + botName;
//		return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? cache.getIfPresent(key).toString() : null)
//						.map(Signal::next), cacheKey)
//				.onCacheMissResume(() -> webClient.get()
//						.uri(builder -> builder.path("admin/bot/search/internal")
//								.queryParam("perPage", 5)
//								.queryParam("page", 1)
//								.queryParam("match", true)
//								.queryParam("name", botName)
//								.build())
//						.retrieve()
//						.bodyToMono(String.class).map(response -> {
//							if (response != null) {
//								ObjectMapper mapper = new ObjectMapper();
//								try {
//									JsonNode root = mapper.readTree(response);
//									if(root.path("result") != null && root.path("result").get(0) != null
//											&& !root.path("result").get(0).isEmpty()) {
//										JsonNode botNode = root.path("result").get(0);
//										return BotUtil.getBotNodeAdapterId(botNode);
//									}
//									return null;
//								} catch (JsonProcessingException jsonMappingException) {
//									return null;
//								}
//
//							} else {
//							}
//							return null;
//						})
//						.doOnError(throwable -> log.info("Error in getting adpater: " + throwable.getMessage()))
//						.onErrorReturn(""))
//				.andWriteWith((key, signal) -> Mono.fromRunnable(
//						() -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
//				.log("cache");
//	}

    /**
     * Retrieve bot id from bot name (from validated bot)
     *
     * @param botName
     * @return
     */
    public Mono<String> getBotIdFromBotName(String botName) {
        String cacheKey = "Bot-id-for-bot-name: " + botName;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? cache.getIfPresent(key).toString() : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get().uri(builder -> builder.path("admin/bot/search/internal")
                                .queryParam("perPage", 5)
                                .queryParam("page", 1)
                                .queryParam("match", true)
                                .queryParam("name", botName)
                                .build())
                        .retrieve().bodyToMono(String.class).map(new Function<String, String>() {
                            @Override
                            public String apply(String response) {
                                if (response != null) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    try {
                                        BotServiceResponse botServiceResponse = mapper.readValue(response, BotServiceResponse.class);
                                        log.info("Bot Service Response : " + botServiceResponse);
                                        if (botServiceResponse != null && botServiceResponse.getResult() != null && botServiceResponse.getResult().size() > 0
                                                && BotUtil.checkBotValidFromResult(botServiceResponse.getResult().get(0))) {
                                            String id = botServiceResponse.getResult().get(0).getId();
                                            if (id != null && !id.isEmpty()) {
                                                return id;
                                            }
                                        }
//                                        JsonNode root = mapper.readTree(response);
//                                        if (root.path("result") != null && root.path("result").get(0) != null
//                                                && !root.path("result").get(0).isEmpty()
//                                                && BotUtil.checkBotValidFromResult(root.path("result").get(0))) {
//                                            return BotUtil.getBotNodeData(root.path("result").get(0), "id");
//                                        }
                                        return null;
                                    } catch (JsonProcessingException jsonMappingException) {
                                        return null;
                                    }

                                } else {
                                }
                                return null;
                            }
                        })
                        .doOnError(throwable -> log.info("Error in getting bot: " + throwable.getMessage()))
                        .onErrorReturn(""))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }

    /**
     * Update fusion auth user - using V1 apis
     *
     * @param userID
     * @param botName
     * @return
     */
    public Mono<Pair<Boolean, String>> updateUser(String userID, String botName) {
        return getBotIdFromBotName(botName).doOnError(e -> log.error(e.getMessage()))
                .flatMap(new Function<String, Mono<Pair<Boolean, String>>>() {
                    @Override
                    public Mono<Pair<Boolean, String>> apply(String botID) {
//						WebClient webClient2 = WebClient.builder().baseUrl(System.getenv("CAMPAIGN_URL_V1")).defaultHeader("admin-token", System.getenv("CAMPAIGN_ADMIN_TOKEN_V1")).build();
                        return webClient.get().uri(new Function<UriBuilder, URI>() {
                                    @Override
                                    public URI apply(UriBuilder builder) {
                                        String base = String.format("/admin/bot/%s/addUser/%s", botID, userID);
                                        URI uri = builder.path(base).build();
                                        return uri;
                                    }
                                }).retrieve().bodyToMono(String.class).map(response -> {
                                    if (response != null) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        try {
                                            BotServiceResponse2 addUserResponse = mapper.readValue(response, BotServiceResponse2.class);
                                            log.info("Bot Service Response : " + addUserResponse);

                                            if (addUserResponse != null && addUserResponse.getResult() != null
                                                    && addUserResponse.getResult().getStatus() != null
                                                    && (addUserResponse.getResult().getStatus().equalsIgnoreCase("USER_ADDED")
                                                    || addUserResponse.getResult().getStatus().equalsIgnoreCase("USER_EXISTS"))) {
                                                String userID = addUserResponse.getResult().getUserId();
                                                return Pair.of(true, userID);
                                            }
                                            return Pair.of(false, "");
                                        } catch (JsonProcessingException jsonMappingException) {
                                            return Pair.of(false, "");
                                        }
                                    } else {
                                        return Pair.of(false, "");
                                    }
                                }).doOnError(throwable -> log.info("Error in updating user: " + throwable.getMessage()))
                                .onErrorReturn(Pair.of(false, ""));
                    }
                });
    }

    /**
     * Get adapter by id
     *
     * @param adapterID
     * @return
     */
    public Mono<Result> getAdapterByID(String adapterID) {
        String cacheKey = "adapter-by-id: " + adapterID;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? (Result) cache.getIfPresent(key) : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get().uri(new Function<UriBuilder, URI>() {
                            @Override
                            public URI apply(UriBuilder builder) {
                                URI uri = builder.path("admin/adapter/" + adapterID).build();
                                return uri;
                            }
                        }).retrieve().bodyToMono(String.class).map(new Function<String, Result>() {
                            @Override
                            public Result apply(String response) {
                                if (response != null) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    try {
                                        BotServiceResponse2 botServiceResponse = mapper.readValue(response, BotServiceResponse2.class);
                                        log.info("Bot Service Response : " + botServiceResponse);
                                        if (botServiceResponse != null && botServiceResponse.getResult() != null
                                                && botServiceResponse.getResult().getId() != null && !botServiceResponse.getResult().getId().isEmpty()) {
                                            return botServiceResponse.getResult();
                                        }
//
//                                        JsonNode root = mapper.readTree(response);
//                                        if (root != null && root.path("result") != null && root.path("result").path("id") != null
//                                                && !root.path("result").path("id").asText().isEmpty()) {
//                                            return root.path("result");
//                                        }
                                        return null;
                                    } catch (JsonProcessingException jsonMappingException) {
                                        return null;
                                    }

                                } else {
                                }
                                return null;
                            }
                        })
                        .doOnError(throwable -> log.info("Error in getting bot: " + throwable.getMessage()))
                        .onErrorReturn(null))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }

    /**
     * Get adapter credentials by id
     *
     * @param adapterID
     * @return
     */
    public Mono<JsonNode> getAdapterCredentials(String adapterID) {
        String cacheKey = "adapter-credentials: " + adapterID;
        return getAdapterByID(adapterID).map(new Function<Result, Mono<JsonNode>>() {
            @Override
            public Mono<JsonNode> apply(Result adapterResult) {
                log.info("adapter: " + adapterResult);
                if (adapterResult != null) {
                    String vaultKey = null;
                    try {
                        if (adapterResult.getConfig() != null && adapterResult.getConfig().getCredentials() != null
                                && adapterResult.getConfig().getCredentials().getVariable() != null) {
                            vaultKey = adapterResult.getConfig().getCredentials().getVariable();
                        } else {
                            log.error("Null values found while getAdapterCredentials : " + adapterResult);
                        }
//                        vaultKey = adapterResult.path("config").path("credentials").path("variable").asText();
                    } catch (Exception ex) {
                        log.error("Exception in fetching adapter variable from json node: " + ex.getMessage());
                        vaultKey = null;
                    }

                    if (vaultKey != null && !vaultKey.isEmpty()) {
                        return getVaultCredentials(vaultKey);
                    }
                } else {
                    log.error("getAdapterByID result null found : " + adapterResult);
                }
                return Mono.just(null);
            }
        }).flatMap(new Function<Mono<JsonNode>, Mono<? extends JsonNode>>() {
            @Override
            public Mono<? extends JsonNode> apply(Mono<JsonNode> n) {
                log.info("Mono FlatMap Level 1");
                return n;
            }
        });
    }

    /**
     * Get Fusion Auth Login Token for vault service
     * @return
     */
    /** NOT IN USE - using admin token directly **/
//    public String getLoginToken() {
//        String cacheKey = "vault-login-token";
//        if(cache.getIfPresent(cacheKey) != null) {
//            log.info("vault user token found");
//            return cache.getIfPresent(cacheKey).toString();
//        }
//        log.info("fetch vault user token");
//        FusionAuthClient fusionAuthClient = new FusionAuthClient(System.getenv("VAULT_FUSION_AUTH_TOKEN"), System.getenv("VAULT_FUSION_AUTH_URL"));
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.loginId = "uci-user";
//        loginRequest.password = "abcd1234";
//        loginRequest.applicationId = UUID.fromString("a1313380-069d-4f4f-8dcb-0d0e717f6a6b");
//        ClientResponse<LoginResponse, Errors> loginResponse = fusionAuthClient.login(loginRequest);
//        if(loginResponse.wasSuccessful()) {
//            cache.put(cacheKey, loginResponse.successResponse.token);
//            return loginResponse.successResponse.token;
//        } else {
//            return null;
//        }
//    }

    /**
     * Retrieve vault credentials from its Identifier
     *
     * @param secretKey - vault key Identifier
     * @return Application
     */
    public Mono<String> getVaultCredentials(String secretKey) {
        String adminToken = System.getenv("VAULT_SERVICE_TOKEN");
        if (adminToken == null || adminToken.isEmpty()) {
            return Mono.just(null);
        }
        WebClient webClient = WebClient.builder().baseUrl(System.getenv("VAULT_SERVICE_URL")).build();
        String cacheKey = "adapter-credentials-by-id: " + secretKey;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? (String) cache.getIfPresent(key) : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                        .uri(builder -> builder.path("admin/secret/" + secretKey).build())
                        .headers(httpHeaders -> {
                            httpHeaders.set("ownerId", "8f7ee860-0163-4229-9d2a-01cef53145ba"); // TODO Need to change
                            httpHeaders.set("ownerOrgId", "org1"); // TODO Need to change
                            httpHeaders.set("admin-token", adminToken);
                        })
                        .retrieve().bodyToMono(String.class).map(response -> {
                            if (response != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                try {
                                    BotServiceResponse2 botServiceResponse = mapper.readValue(response, BotServiceResponse2.class);
                                    log.info("Bot Service Response : " + botServiceResponse);
                                    if (botServiceResponse != null && botServiceResponse.getResult() != null
                                            && botServiceResponse.getResult().getNlAppFirebaseNotification() != null && botServiceResponse.getResult().getNlAppFirebaseNotification().getServiceKey() != null) {
                                        return botServiceResponse.getResult().getNlAppFirebaseNotification().getServiceKey();
                                    }

//                                    Map<String, String> credentials = new HashMap<String, String>();
//                                    JsonNode root = mapper.readTree(response);
//                                    if (root.path("result") != null && root.path("result").path(secretKey) != null) {
//                                        return root.path("result").path(secretKey);
//                                    }
                                    return null;
                                } catch (JsonProcessingException e) {
                                    return null;
                                }
                            }
                            return null;
                        }).doOnError(throwable -> log.info("Error in getting bot: " + throwable.getMessage())).onErrorReturn(null))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");

    }

    /**
     * Retrieve Bot Form Name by its id
     *
     * @param botId - Bot ID
     * @return FormID for the first transformer.
     */
    public Mono<String> getFirstFormByBotID(String botId) {
        String cacheKey = "form-by-bot-name:" + botId;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? cache.getIfPresent(cacheKey).toString() : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                        .uri(builder -> builder.path("admin/bot/" + botId).build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(new Function<String, String>() {
                                 @Override
                                 public String apply(String response) {
                                     if (response != null) {
                                         ObjectMapper mapper = new ObjectMapper();
                                         try {
                                             JsonNode root = mapper.readTree(response);
                                             if (root.path("result") != null && !root.path("result").isEmpty()
                                                     && BotUtil.checkBotValidFromJsonNode(root.path("result"))) {
                                                 return root.path("result").findValue("formID").asText();
                                             }
                                             return null;
                                         } catch (JsonProcessingException e) {
                                             return null;
                                         }
                                     }
                                     return null;
                                 }
                             }
                        )
                        .onErrorReturn(null)
                        .doOnError(throwable -> log.error("Error in getFirstFormByBotID >>> " + throwable.getMessage())))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");

    }

    /**
     * Get Bot Name from its id
     *
     * @param botID
     * @return
     */
    public Mono<String> getBotNameByBotID(String botId) {
        String cacheKey = "bot-name-by-id:" + botId;
        return CacheMono.lookup(key -> Mono.justOrEmpty(cache.getIfPresent(cacheKey) != null ? cache.getIfPresent(cacheKey).toString() : null)
                        .map(Signal::next), cacheKey)
                .onCacheMissResume(() -> webClient.get()
                        .uri(builder -> builder.path("admin/bot/" + botId).build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(new Function<String, String>() {
                                 @Override
                                 public String apply(String response) {
                                     if (response != null) {
                                         ObjectMapper mapper = new ObjectMapper();
                                         try {
                                             JsonNode root = mapper.readTree(response);
                                             if (root.path("result") != null && !root.path("result").isEmpty()
                                                     && BotUtil.checkBotValidFromJsonNode(root.path("result"))) {
                                                 return root.path("result").findValue("name").asText();
                                             }
                                             return null;
                                         } catch (JsonProcessingException e) {
                                             return null;
                                         }
                                     }
                                     return null;
                                 }
                             }
                        )
                        .onErrorReturn(null)
                        .doOnError(throwable -> log.error("Error in getFirstFormByBotID >>> " + throwable.getMessage())))
                .andWriteWith((key, signal) -> Mono.fromRunnable(
                        () -> Optional.ofNullable(signal.get()).ifPresent(value -> cache.put(key, value))))
                .log("cache");
    }

    public Application getButtonLinkedApp(String appName) {
        try {
            Application application = getCampaignFromName(appName);
            String buttonLinkedAppID = (String) ((ArrayList<Map>) application.data.get("parts")).get(0)
                    .get("buttonLinkedApp");
            Application linkedApplication = this.getCampaignFromID(buttonLinkedAppID);
            return linkedApplication;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Application> getApplications() {
        List<Application> applications = new ArrayList<>();
        ClientResponse<ApplicationResponse, Void> response = fusionAuthClient.retrieveApplications();
        if (response.wasSuccessful()) {
            applications = response.successResponse.applications;
        } else if (response.exception != null) {
            Exception exception = response.exception;
        }
        return applications;
    }

    /**
     * Retrieve Campaign Params From its Identifier
     *
     * @param campaignID - Campaign Identifier
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Application getCampaignFromID(String botId) throws Exception {
        ClientResponse<ApplicationResponse, Void> applicationResponse = fusionAuthClient
                .retrieveApplication(UUID.fromString(botId));
        if (applicationResponse.wasSuccessful()) {
            return applicationResponse.successResponse.application;
        } else if (applicationResponse.exception != null) {
            throw applicationResponse.exception;
        }
        return null;
    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param campaignName - Campaign Name
     * @return Application
     */
    private Application getCampaignFromName(String botName) {
        List<Application> applications = getApplications();

        Application currentApplication = null;
        if (applications.size() > 0) {
            for (Application application : applications) {
                if (application.name.equals(botName)) {
                    currentApplication = application;
                }
            }
        }
        return currentApplication;
    }

    /**
     * Retrieve Campaign Params From its Name
     *
     * @param botName - Campaign Name
     * @return Application
     * @throws Exception Error Exception, in failure in Network request.
     */
    public Application getCampaignFromNameESamwad(String botName) {
        List<Application> applications = new ArrayList<>();
        ClientResponse<ApplicationResponse, Void> response = fusionAuthClient.retrieveApplications();
        if (response.wasSuccessful()) {
            applications = response.successResponse.applications;
        } else if (response.exception != null) {
            Exception exception = response.exception;
        }

        Application currentApplication = null;
        if (applications.size() > 0) {
            for (Application application : applications) {
                try {
                    if (application.data.get("appName").equals(botName)) {
                        currentApplication = application;
                    }
                } catch (Exception e) {

                }
            }
        }
        return currentApplication;
    }

    /**
     * Check if response code sent in api response is ok
     *
     * @param responseCode
     * @return Boolean
     */
    private Boolean isApiResponseOk(String responseCode) {
        return responseCode.equals("OK");
    }
}
