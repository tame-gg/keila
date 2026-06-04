// Gale - branding changes - version fetcher

package org.galemc.gale.version;

import com.destroystokyo.paper.PaperVersionFetcher;
import com.destroystokyo.paper.VersionHistoryManager;
import com.destroystokyo.paper.util.VersionFetcher;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import io.papermc.paper.ServerBuildInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.StreamSupport;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.logging.log4j.LogManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

/**
 * An abstract version fetcher, derived from {@link PaperVersionFetcher}.
 * This class was then made to be a superclass of {@link PaperVersionFetcher},
 * {@link GaleVersionFetcher}, and {@link gg.tame.keila.version.KeilaVersionFetcher}.
 * <br>
 * For fork developers, this modified Paper version fetcher makes it easier to
 * register and extend custom version fetchers using existing popular
 * download APIs (e.g., Paper's Fill v3, Bibliothek, or the GitHub API).
 * See the subclasses mentioned above for quick-start examples.
 * <br>
 * Changes to {@link PaperVersionFetcher} are indicated by Gale marker comments.
 */
@NullMarked
public abstract class AbstractPaperVersionFetcher implements VersionFetcher {

    protected static final Logger LOGGER = LogUtils.getClassLogger();
    protected static final ComponentLogger COMPONENT_LOGGER = ComponentLogger.logger(LogManager.getRootLogger().getName());
    protected static final int DISTANCE_ERROR = -1;
    protected static final int DISTANCE_UNKNOWN = -2;
    protected static final ServerBuildInfo BUILD_INFO = loadBuildInfo();

    private static ServerBuildInfo loadBuildInfo() {
        try {
            return ServerBuildInfo.buildInfo();
        } catch (Throwable ex) {
            return null;
        }
    }

    private static final Gson GSON = new Gson();

    // Gale start - branding changes - version fetcher
    protected final String downloadPage;
    protected final String organizationDisplayName;
    protected final String projectDisplayName;
    protected final String gitHubOrganizationName;
    protected final String gitHubRepoName;
    protected final @Nullable String apiUrl;
    protected final @Nullable String userAgent;
    protected final ApiType apiType;

    protected AbstractPaperVersionFetcher(String downloadPage, String organizationDisplayName, String projectDisplayName, String gitHubOrganizationName, String gitHubRepoName, ApiType apiType) {
        this(downloadPage, organizationDisplayName,projectDisplayName, gitHubOrganizationName, gitHubRepoName, null, null, apiType);
    }

    protected AbstractPaperVersionFetcher(String downloadPage, String organizationDisplayName, String projectDisplayName, String gitHubOrganizationName, String gitHubRepoName, @Nullable String apiUrl, @Nullable String userAgent, ApiType apiType) {
        this.downloadPage = downloadPage;
        this.organizationDisplayName = organizationDisplayName;
        this.projectDisplayName = projectDisplayName;
        this.gitHubOrganizationName = gitHubOrganizationName;
        this.gitHubRepoName = gitHubRepoName;
        this.apiUrl = apiUrl;
        this.userAgent = userAgent;
        this.apiType = apiType;
    }
    // Gale end - branding changes - version fetcher

    @Override
    public long getCacheTime() {
        return 720000;
    }

    @Override
    public Component getVersionMessage() {
        final Component updateMessage;
        if (BUILD_INFO == null || (BUILD_INFO.buildNumber().isEmpty() && BUILD_INFO.gitCommit().isEmpty())) { // Gale - branding changes - version fetcher
            updateMessage = text("You are running a development version without access to version information", color(0xFF5300));
        } else {
            updateMessage = getUpdateStatusMessage(this.gitHubOrganizationName + "/" + this.gitHubRepoName, this.downloadPage, this.apiUrl, this.userAgent, this.apiType); // Gale - branding changes - version fetcher
        }
        final Component history = this.getHistory();

        return history != null ? Component.textOfChildren(updateMessage, Component.newline(), history) : updateMessage;
    }

    public static void getUpdateStatusStartupMessage() {
        getUpdateStatusStartupMessage(
            PaperVersionFetcher.REPOSITORY,
            PaperVersionFetcher.DOWNLOAD_PAGE,
            PaperVersionFetcher.API_URL,
            PaperVersionFetcher.USER_AGENT,
            ApiType.FILLV3
        );
    }

    public static void getUpdateStatusStartupMessage(
        final String repository,
        final String downloadPage,
        final @Nullable String apiUrl,
        final @Nullable String userAgent,
        final ApiType apiType
    ) {
        int distance = DISTANCE_ERROR;

        if (BUILD_INFO == null) {
            COMPONENT_LOGGER.warn(text("*** Version information unavailable in this runtime ***"));
            return;
        }
        final OptionalInt buildNumber = BUILD_INFO.buildNumber();
        if (buildNumber.isEmpty() && BUILD_INFO.gitCommit().isEmpty()) {
            COMPONENT_LOGGER.warn(text("*** You are running a development version without access to version information ***"));
        } else {
            final Optional<MinecraftVersionFetcher> apiResult = fetchMinecraftVersionList();
            distance = getDistanceFromApi(repository, apiUrl, userAgent, apiType);

            switch (distance) {
                case DISTANCE_ERROR -> COMPONENT_LOGGER.error(text("*** Error obtaining version information! Cannot fetch version info for " + repository + " ***"));
                case 0 -> apiResult.ifPresent(result -> {
                    COMPONENT_LOGGER.warn(text("*************************************************************************************"));
                    COMPONENT_LOGGER.warn(text("You are running the latest build for your Minecraft version (" + BUILD_INFO.minecraftVersionId() + ")"));
                    COMPONENT_LOGGER.warn(text("However, you are " + result.distance() + " release(s) behind the latest stable release (" + result.latestVersion() + ")!"));
                    COMPONENT_LOGGER.warn(text("It is recommended that you update as soon as possible"));
                    COMPONENT_LOGGER.warn(text(downloadPage));
                    COMPONENT_LOGGER.warn(text("*************************************************************************************"));
                });
                case DISTANCE_UNKNOWN -> COMPONENT_LOGGER.warn(text("*** You are running an unknown version! Cannot fetch version info ***"));
                default -> {
                    if (apiResult.isPresent()) {
                        COMPONENT_LOGGER.warn(text("*** You are running an outdated version of Minecraft, which is " + apiResult.get().distance() + " release(s) and " + distance + " version(s) behind!"));
                        COMPONENT_LOGGER.warn(text("*** Please update to the latest stable version on " + downloadPage + " ***"));
                    } else {
                        COMPONENT_LOGGER.info(text("*** Currently you are " + distance + " version(s) behind ***"));
                        COMPONENT_LOGGER.info(text("*** It is highly recommended to download the latest build from " + downloadPage + " ***"));
                    }
                }
            }
        }
    }

    private static Component getUpdateStatusMessage(final String repo, final String downloadPage, final @Nullable String apiUrl, final @Nullable String userAgent, final ApiType apiType) { // Gale - branding changes - version fetcher
        final int distance = getDistanceFromApi(repo, apiUrl, userAgent, apiType); // Gale - branding changes - version fetcher

        return switch (distance) {
            // Purpur start - PurpurVersionFetcher
            case DISTANCE_ERROR -> text("* Error obtaining version information", NamedTextColor.RED);
            case 0 -> text("* You are running the latest version", NamedTextColor.GREEN);
            case DISTANCE_UNKNOWN -> text("* Unknown version", NamedTextColor.YELLOW);
            default -> text("* You are " + distance + " version(s) behind", NamedTextColor.YELLOW)
                // Purpur end - PurpurVersionFetcher
                .append(Component.newline())
                .append(text("Download the new version at: ")
                    .append(text(downloadPage, NamedTextColor.GOLD) // Gale - branding changes - version fetcher
                        .hoverEvent(text("Click to open", NamedTextColor.WHITE))
                        .clickEvent(ClickEvent.openUrl(downloadPage)))); // Gale - branding changes - version fetcher
        };
    }

    private record MinecraftVersionFetcher(String latestVersion, int distance) {}

    private static Optional<MinecraftVersionFetcher> fetchMinecraftVersionList() {
        final String currentVersion = PaperVersionFetcher.BUILD_INFO.minecraftVersionId();

        try {
            final URL versionsUrl = URI.create("https://fill.papermc.io/v3/projects/paper").toURL();
            final HttpURLConnection connection = (HttpURLConnection) versionsUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", PaperVersionFetcher.USER_AGENT);
            connection.setRequestProperty("Accept", "application/json");

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final JsonObject json = GSON.fromJson(reader, JsonObject.class);
                final JsonObject versions = json.getAsJsonObject("versions");
                final List<String> versionList = versions.keySet().stream()
                    .map(versions::getAsJsonArray)
                    .flatMap(array -> StreamSupport.stream(array.spliterator(), false))
                    .map(JsonElement::getAsString)
                    .toList();

                for (final String latestVersion : versionList) {
                    if (latestVersion.equals(currentVersion)) {
                        return Optional.empty();
                    }

                    try {
                        final URL buildsUrl = URI.create("https://fill.papermc.io/v3/projects/paper/versions/" + latestVersion + "/builds/latest").toURL();
                        final HttpURLConnection connection2 = (HttpURLConnection) buildsUrl.openConnection();
                        connection2.setConnectTimeout(5000);
                        connection2.setReadTimeout(5000);
                        connection2.setRequestProperty("User-Agent", PaperVersionFetcher.USER_AGENT);
                        connection2.setRequestProperty("Accept", "application/json");

                        try (final BufferedReader buildReader = new BufferedReader(new InputStreamReader(connection2.getInputStream(), StandardCharsets.UTF_8))) {
                            final JsonObject buildJson = GSON.fromJson(buildReader, JsonObject.class);
                            if ("STABLE".equals(buildJson.get("channel").getAsString())) {
                                final int currentIndex = versionList.indexOf(currentVersion);
                                final int latestIndex = versionList.indexOf(latestVersion);
                                final int distance = currentIndex - latestIndex;
                                return Optional.of(new MinecraftVersionFetcher(latestVersion, distance));
                            }
                        } catch (final JsonSyntaxException ex) {
                            LOGGER.error("Error parsing json from Paper's downloads API", ex);
                        }
                    } catch (final IOException e) {
                        LOGGER.error("Error while parsing latest build", e);
                    }
                }
            } catch (final JsonSyntaxException ex) {
                LOGGER.error("Error parsing json from Paper's downloads API", ex);
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing version list", e);
        }
        return Optional.empty();
    }

    // Gale start - branding changes - version fetcher
    private static int fetchDistanceFromPaperSiteApi(final int currBuild) {
        return fetchDistanceFromSiteApi(currBuild, PaperVersionFetcher.API_URL, PaperVersionFetcher.USER_AGENT, ApiType.FILLV3);
    }
    private static int fetchDistanceFromPaperGitHub(final String branch, final String hash) {
        return fetchDistanceFromGitHub(PaperVersionFetcher.REPOSITORY, branch, hash, PaperVersionFetcher.USER_AGENT);
    }

    private static int getDistanceFromApi(final String repo, final @Nullable String apiUrl, final @Nullable String userAgent, final ApiType apiType) {
        final Optional<String> gitBranch = BUILD_INFO.gitBranch();
        final Optional<String> gitCommit = BUILD_INFO.gitCommit();

        final boolean hasGitInfo = gitBranch.isPresent() && gitCommit.isPresent();

        if (apiType == ApiType.GITHUB && hasGitInfo) {
            return fetchDistanceFromGitHub(repo, gitBranch.get(), gitCommit.get(), userAgent);
        }

        final OptionalInt buildNumber = BUILD_INFO.buildNumber();
        if (buildNumber.isPresent() && apiUrl != null) {
            return fetchDistanceFromSiteApi(buildNumber.getAsInt(), apiUrl, userAgent, apiType);
        }

        if (hasGitInfo) {
            return fetchDistanceFromGitHub(repo, gitBranch.get(), gitCommit.get(), userAgent);
        }

        return DISTANCE_ERROR;
    }

    private static int getLatestBuildFromFillV3(final BufferedReader reader) {
        final JsonArray builds = GSON.fromJson(reader, JsonArray.class);
        return StreamSupport.stream(builds.spliterator(), false)
            .mapToInt(build -> build.getAsJsonObject().get("id").getAsInt())
            .max()
            .orElseThrow();
    }

    private static int getLatestBuildFromBibliothekApi(final BufferedReader reader) {
        final JsonArray builds = GSON.fromJson(reader, JsonObject.class).getAsJsonArray("builds");
        return StreamSupport.stream(builds.spliterator(), false)
            .mapToInt(build -> build.getAsJsonObject().get("build").getAsInt())
            .max()
            .orElseThrow();
    }
    // Gale end - branding changes - version fetcher

    protected static int fetchDistanceFromSiteApi(final int currBuild, final String apiUrl, final @Nullable String userAgent, final ApiType apiType) { // Gale - branding changes - version fetcher
        try {
            final URL buildsUrl = URI.create(apiUrl).toURL(); // Gale - branding changes - version fetcher
            final HttpURLConnection connection = (HttpURLConnection) buildsUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (userAgent != null) connection.setRequestProperty("User-Agent", userAgent); // Gale - branding changes - version fetcher
            connection.setRequestProperty("Accept", "application/json");
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final int latestBuildId = apiType == ApiType.FILLV3 ? getLatestBuildFromFillV3(reader) : getLatestBuildFromBibliothekApi(reader); // Gale - branding changes - version fetcher
                return Math.max(latestBuildId - currBuild, 0); // Gale - branding changes - version fetcher
            } catch (final JsonSyntaxException ex) {
                LOGGER.error("Error parsing json from {}'s downloads API", BUILD_INFO.brandName(), ex); // Gale - branding changes - version fetcher
                return DISTANCE_ERROR;
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing version", e);
            return DISTANCE_ERROR;
        }
    }

    // Contributed by Techcable <Techcable@outlook.com> in GH-65
    private static int fetchDistanceFromGitHub(final String repo, final String branch, final String hash, final @Nullable String userAgent) { // Gale - branding changes - version fetcher
        if ("unknown".equalsIgnoreCase(branch) || "unknown".equalsIgnoreCase(hash) || hash.isBlank()) {
            return DISTANCE_UNKNOWN;
        }

        final String compareBranch = "HEAD".equalsIgnoreCase(branch) ? resolveDefaultBranch(repo, userAgent) : branch;
        if (compareBranch == null) {
            LOGGER.warn("Unable to resolve default branch for {}; version check skipped", repo);
            return DISTANCE_UNKNOWN;
        }

        try {
            final HttpURLConnection connection = (HttpURLConnection) URI.create("https://api.github.com/repos/%s/compare/%s...%s".formatted(repo, compareBranch, hash)).toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (userAgent != null) {
                connection.setRequestProperty("User-Agent", userAgent);
            }
            connection.setRequestProperty("Accept", "application/vnd.github+json");
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return DISTANCE_UNKNOWN;
            }
            if (responseCode != HttpURLConnection.HTTP_OK) {
                LOGGER.error("GitHub version check for {} failed with HTTP {} (branch={}, commit={})", repo, responseCode, compareBranch, hash);
                return DISTANCE_ERROR;
            }
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                final String status = obj.get("status").getAsString();
                return switch (status) {
                    case "identical" -> 0;
                    case "behind" -> obj.get("behind_by").getAsInt();
                    default -> DISTANCE_ERROR;
                };
            } catch (final JsonSyntaxException | NumberFormatException e) {
                LOGGER.error("Error parsing json from GitHub's API for {}", repo, e);
                return DISTANCE_ERROR;
            }
        } catch (final IOException e) {
            LOGGER.error("Error while checking version for {}", repo, e);
            return DISTANCE_ERROR;
        }
    }

    private static @Nullable String resolveDefaultBranch(final String repo, final @Nullable String userAgent) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) URI.create("https://api.github.com/repos/%s".formatted(repo)).toURL().openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (userAgent != null) {
                connection.setRequestProperty("User-Agent", userAgent);
            }
            connection.setRequestProperty("Accept", "application/vnd.github+json");
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.error("GitHub repository lookup for {} failed with HTTP {}", repo, connection.getResponseCode());
                return null;
            }
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                final JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                return obj.get("default_branch").getAsString();
            }
        } catch (final IOException | JsonSyntaxException e) {
            LOGGER.error("Error resolving default branch for {}", repo, e);
            return null;
        }
    }

    private @Nullable Component getHistory() {
        final VersionHistoryManager.@Nullable VersionData data = VersionHistoryManager.INSTANCE.getVersionData();
        if (data == null) {
            return null;
        }

        final String oldVersion = data.getOldVersion();
        if (oldVersion == null) {
            return null;
        }

        return text("Previous: " + oldVersion, NamedTextColor.GRAY, TextDecoration.ITALIC); // Purpur - Rebrand
    }

    // Gale start - branding changes - version fetcher
    public enum ApiType {
        FILLV3,
        BIBLIOTHEK,
        GITHUB
    }
    // Gale end - branding changes - version fetcher
}
