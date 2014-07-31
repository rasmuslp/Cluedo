package cluedo.common.message;

public enum CluedoMessageType {

	// Server Messages

	S_DEFINITION,

	S_GAME_START,

	S_GAME_END,

	S_HAND_CARD,

	S_TURN_START,

	S_DISPROVE_REQ,

	// Client Messages

	C_SUGGESTION,

	C_ACCUSATION,

	C_TURN_END,

	// Common Messages

	DISPROVE,

}
