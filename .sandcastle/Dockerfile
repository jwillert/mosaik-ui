FROM eclipse-temurin:21-jdk

# Install system dependencies
RUN apt-get update && apt-get install -y \
  git \
  curl \
  jq \
  unzip \
  && rm -rf /var/lib/apt/lists/*

# Install GitHub CLI
RUN curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg \
  | dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg \
  && echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" \
  | tee /etc/apt/sources.list.d/github-cli.list > /dev/null \
  && apt-get update && apt-get install -y gh \
  && rm -rf /var/lib/apt/lists/*

# Install Node.js (needed for Tailwind CSS tooling via npm/npx)
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - \
  && apt-get install -y nodejs \
  && rm -rf /var/lib/apt/lists/*

# Build-args for UID/GID alignment
ARG AGENT_UID=1000
ARG AGENT_GID=1000

# Rename the base image's existing user (ubuntu) to agent and align UID/GID
RUN groupmod -o -g $AGENT_GID ubuntu && usermod -o -u $AGENT_UID -g $AGENT_GID -d /home/agent -m -l agent ubuntu
USER ${AGENT_UID}:${AGENT_GID}

# Pre-cache Gradle 8.14 distribution by running the wrapper once at image build time.
# The wrapper downloads, unpacks, and registers the distribution in the correct
# hash-named directory (~/.gradle/wrapper/dists/gradle-8.14-bin/<hash>/) so agents
# never have to download it at runtime.
COPY --chown=${AGENT_UID}:${AGENT_GID} gradle /tmp/gradle-bootstrap/gradle
COPY --chown=${AGENT_UID}:${AGENT_GID} gradlew /tmp/gradle-bootstrap/gradlew
RUN cd /tmp/gradle-bootstrap \
  && chmod +x gradlew \
  && ./gradlew --version \
  && rm -rf /tmp/gradle-bootstrap

# Install Claude Code CLI
RUN curl -fsSL https://claude.ai/install.sh | bash

# Add Claude and Gradle to PATH
ENV PATH="/home/agent/.local/bin:$PATH"

# 1. Definiere einen neuen Pfad für NPM "global" innerhalb des User-Verzeichnisses
ENV NPM_CONFIG_PREFIX=/home/agent/.npm-global
# 2. Füge diesen Pfad zum PATH hinzu, damit der Befehl 'pi-coding-agent' gefunden wird
ENV PATH=$PATH:/home/agent/.npm-global/bin

# Install Pi Agent
RUN npm install -g @earendil-works/pi-coding-agent

   # Erstelle das Verzeichnis für die Konfiguration
RUN mkdir -p /home/agent/.pi/agent

# Erstelle die models.json mit gemma4:26b
RUN echo '{"providers": {"ollama": {"api": "openai-completions", "apiKey": "ollama", "baseUrl": "http://host.containers.internal:11434/v1", "models": [{"_launch": true, "id": "gemma4:26b", "input": ["text"], "reasoning": true}, {"_launch": true, "contextWindow": 32768, "id": "qwen2.5:7b", "input": ["text"]}]}}}' > /home/agent/.pi/agent/models.json

   # Erstelle die settings.json mit gemma4:26b
RUN echo '{ "defaultModel": "qwen2.5:7b", "defaultProvider": "ollama", "lastChangelogVersion": "0.80.1", "packages": ["npm:@ollama/pi-web-search"], "theme": "dark"}' > /home/agent/.pi/agent/settings.json

WORKDIR /home/agent

ENTRYPOINT ["sleep", "infinity"]
