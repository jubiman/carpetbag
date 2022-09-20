package us.jusybiberman.carpetbag.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.jusybiberman.carpetbag.api.capability.ICarpetbagPlayer;
import us.jusybiberman.carpetbag.capability.CPBCapabilityManager;
import us.jusybiberman.carpetbag.capability.CarpetbagPlayer;

public class S2CSyncCarpetbagCapMsg extends BaseMsg {
	private ICarpetbagPlayer playerCap;
	private ByteBuf clientBuf;

	@Deprecated // pls dont use, its a must have sadly for IMessage
	public S2CSyncCarpetbagCapMsg() {
	}

	public S2CSyncCarpetbagCapMsg(ICarpetbagPlayer playerCap) {
		this.playerCap = playerCap;
	}

	@Override
	public void write(PacketBuffer buffer) {
		CarpetbagPlayer.Serializer.INSTANCE.writeToBuffer(playerCap, buffer);
	}

	@Override
	public void read(PacketBuffer buffer) {
		this.clientBuf = buffer.copy();
	}

	public static class Handler implements IMessageHandler<S2CSyncCarpetbagCapMsg, IMessage> {
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(S2CSyncCarpetbagCapMsg message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				PacketBuffer packetBuffer = new PacketBuffer(Unpooled.wrappedBuffer(message.clientBuf));

				ICarpetbagPlayer carpetbagPlayer = CPBCapabilityManager.asCarpetbagPlayer(Minecraft.getMinecraft().player);
				CarpetbagPlayer.Serializer.INSTANCE.readFromBuffer(carpetbagPlayer, packetBuffer);
			});

			return null;
		}
	}
}
