package cluedo.common.message;

import glhf.common.entity.EntityList;
import glhf.common.entity.list.IntegerList;
import glhf.common.entity.list.StringList;

import java.io.IOException;

import cluedo.common.cards.Card;
import cluedo.common.cards.ThreeCardPack;
import cluedo.common.definition.Definition;
import cluedo.common.message.client.AccusationMessage;
import cluedo.common.message.client.SuggestionMessage;
import cluedo.common.message.client.TurnEndMessage;
import cluedo.common.message.common.DisproveMessage;
import cluedo.common.message.server.DefinitionMessage;
import cluedo.common.message.server.DisproveRequestMessage;
import cluedo.common.message.server.GameEndMessage;
import cluedo.common.message.server.GameStartMessage;
import cluedo.common.message.server.HandCardMessage;
import cluedo.common.message.server.TurnStartMessage;
import crossnet.log.Log;
import crossnet.message.AbstractMessageParser;
import crossnet.message.Message;
import crossnet.util.ByteArrayReader;

public class CluedoMessageParser extends AbstractMessageParser< CluedoMessageType > {

	public CluedoMessageParser() {
		super( CluedoMessageType.class );
	}

	@Override
	protected Message parseType( CluedoMessageType messageType, ByteArrayReader payload ) {
		Message message = null;

		try {
			switch ( messageType ) {

			// Server Messages

				case S_DEFINITION: {
					StringList lines = new StringList();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						String line = payload.readString255();
						lines.add( line );
					}
					message = new DefinitionMessage( lines );
					break;
				}

				case S_GAME_START: {
					IntegerList ids = new IntegerList();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						int id = payload.readInt();
						ids.add( id );
					}
					message = new GameStartMessage( ids );
					break;
				}

				case S_GAME_END: {
					int id = payload.readInt();
					ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
					message = new GameEndMessage( id, threeCardPack );
					break;
				}

				case S_HAND_CARD: {
					String cardID = payload.readString255();
					Card card = Definition.definition.getAllCards().get( cardID );
					message = new HandCardMessage( card );
					break;
				}

				case S_TURN_START: {
					int id = payload.readInt();
					message = new TurnStartMessage( id );
					break;
				}

				case S_DISPROVE_REQ: {
					ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
					message = new DisproveRequestMessage( threeCardPack );
					break;
				}

				// Client Messages

				case C_SUGGESTION: {
					ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
					message = new SuggestionMessage( threeCardPack );
					break;
				}

				case C_ACCUSATION: {
					ThreeCardPack threeCardPack = ThreeCardPack.parse( payload );
					message = new AccusationMessage( threeCardPack );
					break;
				}

				case C_TURN_END: {
					message = new TurnEndMessage();
					break;
				}

				// Common Messages

				case DISPROVE: {
					EntityList< Card > cards = new EntityList<>();
					int count = payload.readInt();
					for ( int i = 0; i < count; i++ ) {
						String cardID = payload.readString255();
						cards.add( Definition.definition.getAllCards().get( cardID ) );
					}
					message = new DisproveMessage( cards );
					break;
				}

				default:
					Log.error( "Cluedo-common", "Unknown CluedoMessageType, cannot parse: " + messageType );
					break;
			}
		} catch ( IOException e ) {
			Log.error( "Cluedo-common", "Error deserializing CluedoMessage of type:" + messageType, e );
		}

		return message;
	}

}
