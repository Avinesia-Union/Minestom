package net.minestom.server.potion;

import net.minestom.server.entity.Entity;
import net.minestom.server.network.packet.server.play.EntityEffectPacket;
import net.minestom.server.network.packet.server.play.RemoveEntityEffectPacket;
import net.minestom.server.utils.binary.BinaryReader;
import org.jetbrains.annotations.NotNull;

public record Potion(PotionEffect effect, byte amplifier, int duration, byte flags) {
    public Potion(BinaryReader reader) {
        this(PotionEffect.fromId(reader.readVarInt()), reader.readByte(), reader.readVarInt(), reader.readByte());
    }

    /**
     * Sends a packet that a potion effect has been applied to the entity.
     * <p>
     * Used internally by {@link net.minestom.server.entity.Player#addEffect(Potion)}
     *
     * @param entity the entity to add the effect to
     */
    public void sendAddPacket(@NotNull Entity entity) {
        entity.sendPacketToViewersAndSelf(new EntityEffectPacket(entity.getEntityId(), this));
    }

    /**
     * Sends a packet that a potion effect has been removed from the entity.
     * <p>
     * Used internally by {@link net.minestom.server.entity.Player#removeEffect(PotionEffect)}
     *
     * @param entity the entity to remove the effect from
     */
    public void sendRemovePacket(@NotNull Entity entity) {
        RemoveEntityEffectPacket removeEntityEffectPacket = new RemoveEntityEffectPacket();
        removeEntityEffectPacket.entityId = entity.getEntityId();
        removeEntityEffectPacket.effect = effect;
        entity.sendPacketToViewersAndSelf(removeEntityEffectPacket);
    }
}
