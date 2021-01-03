package net.nonswag.tnl.listener.api.mojangAPI;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PlayerProfile {

	private final UUID uuid;
	private final String username;
	private final Set<Property> properties;
	private final Optional<TexturesProperty> textures;

	public static class Property {
		String name;
		String value;
		String signature;

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getSignature() {
			return signature;
		}
	}

	public static class TexturesProperty extends Property {
		long timestamp;
		String profileId, profileName;
		boolean signatureRequired = false;
		Map<String, URL> textures;

		public long getTimestamp() {
			return timestamp;
		}

		public String getProfileId() {
			return profileId;
		}

		public String getProfileName() {
			return profileName;
		}

		public boolean isSignatureRequired() {
			return signatureRequired;
		}

		public Map<String, URL> getTextures() {
			return textures;
		}

		public Optional<URL> getSkin() {
			return Optional.ofNullable(textures.get("SKIN"));
		}

		public Optional<URL> getCape() {
			return Optional.ofNullable(textures.get("CAPE"));
		}
	}

	public PlayerProfile(UUID uuid, String username, Set<Property> properties) {
		this.uuid = uuid;
		this.username = username;
		this.properties = properties;
		this.textures = properties.stream().filter(p -> p.getName().equals("textures")).map(p -> (TexturesProperty) p).findAny();
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getUsername() {
		return username;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public Optional<TexturesProperty> getTextures() {
		return textures;
	}
}