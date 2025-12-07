# ConvoCraft

Small chatroom/messaging project with a native profanity filter component.
Developed by Aditya S Rao (IMT2023061) and Mannat Bagga (IMT2023071)
---

## Quick summary

- Language: Java (target JDK 17)  
- Build: Maven  
- Native component: C++ profanityFilter (produces `.so` / `.dll`) under `src/main/cpp`  
- Tests: JUnit 4 suite at `src/test/java/com/convocraft/GeneratedTestsSuite.java` (runs Unit, Integration, System style checks)
- Resources: `src/main/resources/badWords.txt`, `profanityFilter.so` and `profanityFilter.dll`

---

## Prerequisites

- Linux (tested)
- OpenJDK 17 (recommended) or OpenJDK 21 if you want to build/run with Java 21
- Maven 3.x
- (optional) build tools for native code (g++, make) if you need to recompile the C++ native library

Install on Ubuntu/Debian:
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven build-essential
```

Verify:
```bash
java -version
javac -version
mvn -v
```

---

## Project layout (high level)

- src/main/java/com/convocraft
  - App.java — application entry point
  - MessageSender / MessageReceiver — messaging helpers
  - chatroom/* — chatroom and connection support
  - chatroomManager/* — user/admin management
  - cmdManager/* — terminal interactions and room commands
  - commandProcessor/*
    - profanityFilter.java / profanityFilter.class — Java wrapper for native profanity checking
- src/main/cpp — native profanity filter implementation (C++ / JNI)
- src/main/resources — badWords.txt, native libs
- src/test/java/com/convocraft/GeneratedTestsSuite.java — generated test suite
- target/ — build outputs, compiled classes, test reports

---

## Build and run

From project root (`~/Documents/ConvoCraft`):

1. Clean and build:
```bash
mvn clean package
```

2. Run the packaged jar:
```bash
java -jar target/convocraft-1.0.jar
```

If you need the native library to be found at runtime, either
- place the `.so` / `.dll` in a folder on `java.library.path`, or
- start JVM with:
```bash
java -Djava.library.path=target/classes -jar target/convocraft-1.0.jar
```
(`target/classes` already contains the bundled native libs when packaged by default in this project.)

---

## Tests

Run all project tests (default):
```bash
mvn test
```

Run the generated suite explicitly:
```bash
mvn -Dtest=com.convocraft.GeneratedTestsSuite test
```

Test reports:
- Surefire (unit) text & XML: `target/surefire-reports/`
- Failsafe (integration/system): `target/failsafe-reports/` (created when integration tests run)

Notes:
- The GeneratedTestsSuite performs read-only checks (reflection, resource & file checks).
- Some tests avoid initializing classes that would trigger native library loads (so tests don't fail when native libs are missing).
- If you see "compiled by a more recent version" errors, set the compiler release to match your JDK or install the required JDK version (see Troubleshooting).

---

## Troubleshooting

- "release version 21 not supported" or "class file version 65.0"  
  - Either install JDK 21 or set the project to compile for Java 17. Current project POM targets Java 17. To enforce Java 17 compile target ensure:
  ```xml
  <properties>
    <maven.compiler.release>17</maven.compiler.release>
  </properties>
  ```
  and run:
  ```bash
  mvn clean test
  ```

- UnsatisfiedLinkError: `no profanityFilter in java.library.path`  
  - Ensure the native library (`profanityFilter.so` or `profanityFilter.dll`) is on `java.library.path`. Example:
  ```bash
  java -Djava.library.path=src/main/resources -jar target/convocraft-1.0.jar
  ```
  - Tests avoid performing native loads where possible; if a class triggers native loading in a static initializer, tests may fail unless native lib is present.

- To recompile native library:
```bash
# example (depends on your platform and Makefile)
cd src/main/cpp
make
# copy resulting .so into src/main/resources or add to java.library.path
```

---

## Features & components (short)

- Messaging via ActiveMQ (client & broker dependencies included) — chatrooms and message routing.
- Chatroom management: create/join rooms, user/admin roles.
- Terminal-based command manager for user interaction (two terminal implementations in `cmdManager`).
- Profanity filter implemented as JNI-backed native module for performance; Java wrapper provided to integrate with the command processor.
- Resource-based bad words list (`badWords.txt`) used by the filter and tests.

---

## Design → Implementation → Testing: what changed

- Requirements/design phase (see `requirements analysis and design phase/`):
  - Includes SRS and UML diagrams (Activity, Sequence, Class, StateChart, Object).
  - High-level design described system components, interactions and native filter requirement.

- Implementation:
  - Packages and classes were implemented per design: chatroom, manager, command processor.
  - Native profanity filter implemented in `src/main/cpp` and exposed via JNI; a Java wrapper exists.
  - Some class or method names may have been adjusted during implementation for practical reasons (e.g., `TerminalInteraction2` added for an alternate terminal implementation).
  - Project POM was aligned to target JDK 17 for compatibility with local environment; originally some generated files targeted Java 21 and required fixing.

- Testing:
  - A generated JUnit suite (`GeneratedTestsSuite`) was added to perform read-only checks (resource existence, class presence, method signatures, project structure).
  - Tests were hardened to avoid side-effects and to handle the native-library loading issue by avoiding static initialization where needed.
  - Test harness (`test_pom.xml`) was provided to demonstrate running tests via Surefire and Failsafe; in practice Surefire runs the suite successfully and failsafe runs no ITs unless configured to include them.

Overall the design informed package structure and component responsibilities; implementation introduced practical adjustments (naming, JNI wiring, POM tweaks). Tests were created to be safe, deterministic and to validate presence/shape of components rather than perform heavy integration with external systems.
