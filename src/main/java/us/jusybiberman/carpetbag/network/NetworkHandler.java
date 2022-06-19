package us.jusybiberman.carpetbag.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import us.jusybiberman.carpetbag.Carpetbag;

public class NetworkHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Carpetbag.MOD_ID + "_channel");
	private static int id = 0;

	@SuppressWarnings("Convert2MethodRef") // never convert it to method ref here because it causes crashes
	public static void registerPackets() {
		registerMessage(S2CSyncCarpetbagCapMsg.class, S2CSyncCarpetbagCapMsg.Handler.class, Side.CLIENT);
		//registerMessage(S2CKickPlayerFromSPMsg.class, S2CKickPlayerFromSPMsg.Handler.class, Side.CLIENT);

		/* TODO: Maybe add back in when and if we add dialog quests
		https://github.com/TheSlayerMC/Journey-Into-The-Light/ UNLICENSED CODE LMAOOOO
		DialogNetHandler dialogueNetHandler = getDialogNetHandler();

		registerDialogPacket(S2COpenDialogGuiMsg.class, (message, ctx) -> dialogueNetHandler.handleDialogOpenPacket(message, ctx), Side.CLIENT);
		registerDialogPacket(S2CCloseDialogGuiMsg.class, (message, ctx) -> dialogueNetHandler.handleDialogClosePacket(message, ctx), Side.CLIENT);
		registerDialogPacket(C2SChosenOptionMsg.class, (message, ctx) -> dialogueNetHandler.handlePressOptionPacket(message, ctx), Side.SERVER);
		*/
	}


	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> packetClass, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Side side) {
		INSTANCE.registerMessage(messageHandler, packetClass, id++, side);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> packetClass, IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Side side) {
		INSTANCE.registerMessage(messageHandler, packetClass, id++, side);
	}

	/*private static <REQ extends IMessage> void registerDialogPacket(Class<REQ> packetClass, BiConsumer<REQ, MessageContext> handler, Side side) {
		registerMessage(packetClass, new DialogPacketHandler<>(handler), side);
	}

	private static DialogNetHandler getDialogNetHandler() {
		return JManagers.DIALOGUE_MANAGER.getNetHandler();
	}*/
}
