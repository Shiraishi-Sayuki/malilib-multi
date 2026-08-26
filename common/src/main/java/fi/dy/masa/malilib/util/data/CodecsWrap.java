package fi.dy.masa.malilib.util.data;

import java.util.List;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;

/**
 * Cloned from 1.21.4+
 */
public class CodecsWrap extends Codecs
{
	public static <E> Codec<List<E>> listOrSingle(Codec<E> entryCodec)
	{
		return listOrSingle(entryCodec, entryCodec.listOf());
	}

	public static <E> Codec<List<E>> listOrSingle(Codec<E> entryCodec, Codec<List<E>> listCodec)
	{
		return Codec.either(listCodec, entryCodec)
		            .xmap(either -> either.map(list -> list, List::of),
		                  list ->
		                  {
		                      // 型推論の揺れを避けるため型証明を明示する
		                      if (list.size() == 1)
		                      {
		                          return com.mojang.datafixers.util.Either.<List<E>, E>right(list.get(0));
		                      }
		                      return com.mojang.datafixers.util.Either.<List<E>, E>left(list);
		                  });
	}
}
