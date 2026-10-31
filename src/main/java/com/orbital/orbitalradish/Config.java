package com.orbital.orbitalradish;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("orbitalradish.json");

    public static boolean logDirtBlock = true;
    public static int magicNumber = 42;
    public static String magicNumberIntroduction = "The magic number is... ";

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            Data data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                logDirtBlock = data.logDirtBlock;
                magicNumber = data.magicNumber;
                magicNumberIntroduction = data.magicNumberIntroduction;
            }
        } catch (IOException ignored) {
        }
    }

    public static void save() {
        Data data = new Data();
        data.logDirtBlock = logDirtBlock;
        data.magicNumber = magicNumber;
        data.magicNumberIntroduction = magicNumberIntroduction;

        try (Writer writer = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            GSON.toJson(data, writer);
        } catch (IOException ignored) {
        }
    }

    private static final class Data {
        boolean logDirtBlock = true;
        int magicNumber = 42;
        String magicNumberIntroduction = "The magic number is... ";
    }

    private Config() {
    }
}
