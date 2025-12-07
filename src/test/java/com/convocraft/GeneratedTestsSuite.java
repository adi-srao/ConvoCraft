// ...existing code...
package com.convocraft;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Generated test suite containing unit, integration and system style tests.
 * This file groups multiple checks that are read-only and use reflection/filesystem.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GeneratedTestsSuite.UnitTests.class,
        GeneratedTestsSuite.IntegrationTests.class,
        GeneratedTestsSuite.SystemTests.class
})
public class GeneratedTestsSuite {

    public static class UnitTests {
        @Test
        public void badWordsResourceExistsAndNotEmpty() throws Exception {
            // Try common possible resource locations
            InputStream in = getClass().getClassLoader().getResourceAsStream("badWords.txt");
            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream("resources/badWords.txt");
            }
            assertNotNull("badWords.txt resource should be present on the classpath", in);

            List<String> words;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                words = br.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
            }

            assertTrue("badWords.txt should contain at least 3 entries", words.size() >= 3);

            // Ensure entries are trimmed (no leading/trailing whitespace)
            for (String w : words) {
                assertEquals("word entry should be trimmed", w, w.trim());
            }

            // Ensure entries are unique ignoring case
            Set<String> lower = words.stream().map(String::toLowerCase).collect(Collectors.toSet());
            assertEquals("badWords.txt should not contain duplicate entries (case-insensitive)", lower.size(), words.size());

            // Ensure entries are not just punctuation / control characters
            boolean hasAlpha = words.stream().anyMatch(s -> s.codePoints().anyMatch(Character::isLetter));
            assertTrue("At least one bad word entry should contain alphabetic characters", hasAlpha);
        }

        @Test
        public void appClassIsPackaged() {
            URL url = getClass().getClassLoader().getResource("com/convocraft/App.class");
            assertNotNull("App.class should be available on the classpath", url);
        }

        @Test
        public void sourceFilesExistInProjectTree() {
            // When running under Maven, user.dir is project root
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path srcMain = projectRoot.resolve("src").resolve("main").resolve("java");

            List<String> expected = Arrays.asList(
                    "com/convocraft/App.java",
                    "com/convocraft/MessageSender.java",
                    "com/convocraft/MessageReceiver.java",
                    "com/convocraft/commandProcessor/profanityFilter.java"
            );

            for (String rel : expected) {
                Path p = srcMain.resolve(rel);
                assertTrue("Expected source file present: " + p, Files.exists(p));
                try {
                    assertTrue("Expected non-empty source file: " + p, Files.size(p) > 0);
                } catch (java.io.IOException e) {
                    fail("Unable to read file size for " + p + ": " + e.getMessage());
                }
            }
        }
    }

    public static class IntegrationTests {
        @Test
        public void profanityFilterClassFilePresent() {
            URL url = getClass().getClassLoader().getResource("com/convocraft/commandProcessor/profanityFilter.class");
            assertNotNull("profanityFilter.class should be present on the classpath", url);
        }

        @Test
        public void eitherSourceOrClassForProfanityFilterAvailable() {
            boolean hasClass = getClass().getClassLoader().getResource("com/convocraft/commandProcessor/profanityFilter.class") != null;
            boolean hasSource = getClass().getClassLoader().getResource("com/convocraft/commandProcessor/profanityFilter.java") != null;
            assertTrue("Either profanityFilter.class or profanityFilter.java should be available", hasClass || hasSource);
        }

        @Test
        public void keyClassesArePresent() {
            String[] classes = {
                    "com.convocraft.MessageSender",
                    "com.convocraft.MessageReceiver",
                    "com.convocraft.chatroom.Chatroom",
                    "com.convocraft.chatroomManager.Admin",
                    "com.convocraft.cmdManager.TerminalInteraction"
            };
            for (String cn : classes) {
                try {
                    Class.forName(cn);
                } catch (ClassNotFoundException e) {
                    fail("Expected class on classpath: " + cn);
                }
            }
        }
    }

    public static class SystemTests {
        @Test
        public void nativeLibrariesPresentAndReasonableSize() throws Exception {
            URL so = getClass().getClassLoader().getResource("profanityFilter.so");
            URL dll = getClass().getClassLoader().getResource("profanityFilter.dll");
            assertTrue("At least one native library (profanityFilter.so or profanityFilter.dll) should be present", so != null || dll != null);

            // If present, check resource length > 512 bytes (simple sanity)
            if (so != null) {
                try (InputStream in = so.openStream()) {
                    byte[] data = in.readAllBytes();
                    assertTrue(".so file appears too small", data.length > 512);
                }
            }
            if (dll != null) {
                try (InputStream in = dll.openStream()) {
                    byte[] data = in.readAllBytes();
                    assertTrue(".dll file appears too small", data.length > 512);
                }
            }
        }

        @Test
        public void appHasMainMethodSignature() throws Exception {
            Class<?> appClass = Class.forName("com.convocraft.App");
            java.lang.reflect.Method main = appClass.getMethod("main", String[].class);
            assertNotNull("App.main should be present", main);
            assertTrue("App.main should be static", Modifier.isStatic(main.getModifiers()));
        }

        @Test
        public void testProjectStructureStable() {
            // Verify important folders exist (non-destructive)
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            assertTrue("src/main/java should exist", Files.isDirectory(projectRoot.resolve("src/main/java")));
            assertTrue("src/main/resources should exist", Files.isDirectory(projectRoot.resolve("src/main/resources")));
            assertTrue("src/test/java should exist", Files.isDirectory(projectRoot.resolve("src/test/java")));
        }
    }
}
// ...existing code...