package vulpisgiardina.commandrod;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record ClickCommandData(Optional<String> right, Optional<String> left) {
    public static final ClickCommandData NULL = new ClickCommandData(Optional.of(""), Optional.empty());

    private static final Codec<ClickCommandData> OBJECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("right").forGetter(ClickCommandData::right),
            Codec.STRING.optionalFieldOf("left").forGetter(ClickCommandData::left)
    ).apply(instance, ClickCommandData::new));

    public static final Codec<ClickCommandData> CODEC = Codec.either(Codec.STRING, OBJECT_CODEC).xmap(
            // デコード
            either -> either.map(
                    str -> new ClickCommandData(Optional.of(str), Optional.empty()),
                    obj -> obj
            ),
            // エンコード
            data -> Either.right(data)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ClickCommandData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ClickCommandData::right,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), ClickCommandData::left,
            ClickCommandData::new
    );
}
