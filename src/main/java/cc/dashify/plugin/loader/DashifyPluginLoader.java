package cc.dashify.plugin.loader;

import com.google.gson.Gson;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author lambdynma
 * <p>
 * This file is intended to be used by the PaperMC Paper Plugins mechanism.
 * <p>
 * Loads the plugin dependencies from a JSON file.
 * <p>
 * This file should not be used by any other means.
 */

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class DashifyPluginLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        PluginLibraries pluginLibraries = load();
        pluginLibraries.asDependencies().forEach(resolver::addDependency);
        pluginLibraries.asRepositories().forEach(resolver::addRepository);
        classpathBuilder.addLibrary(resolver);
    }

    public PluginLibraries load() {
        PluginLibraries libraries = null;

        try (var in = getClass().getResourceAsStream("/paper-libraries.json")) {
            assert in != null;

            InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            libraries = new Gson().fromJson(reader, PluginLibraries.class);
        } catch (IOException e) {
            System.err.println("Failed to load libraries from paper-libraries.json");
            e.printStackTrace();
        }

        return libraries;
    }

    private record PluginLibraries(Map<String, String> repositories, List<String> dependencies) {
        public Stream<Dependency> asDependencies() {
            return dependencies.stream()
                    .map(d -> new Dependency(new DefaultArtifact(d), null));
        }

        public Stream<RemoteRepository> asRepositories() {
            return repositories.entrySet().stream()
                    .map(e -> new RemoteRepository.Builder(e.getKey(), "default", e.getValue()).build());
        }
    }
}