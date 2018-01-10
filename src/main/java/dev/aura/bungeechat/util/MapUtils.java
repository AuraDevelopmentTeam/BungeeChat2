package dev.aura.bungeechat.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MapUtils {
  /**
   * A variant of {@link Collectors#toMap(Function, Function)} for immutable maps.
   *
   * <p>Note this variant throws {@link IllegalArgumentException} upon duplicate keys, rather than
   * {@link IllegalStateException}
   *
   * @param <T> type of the input elements
   * @param <K> output type of the key mapping function
   * @param <V> output type of the value mapping function
   * @param keyMapper a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @return a {@code Collector} which collects elements into a {@code Map} whose keys and values
   *     are the result of applying mapping functions to the input elements
   * @throws IllegalArgumentException upon duplicate keys
   */
  public static <T, K, V>
      Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> immutableMapCollector(
          Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper)
          throws IllegalArgumentException {
    return new Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>>() {
      public Supplier<ImmutableMap.Builder<K, V>> supplier() {
        return ImmutableMap.Builder<K, V>::new;
      }

      public BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator() {
        return (builder, element) -> {
          K key = keyMapper.apply(element);
          V value = valueMapper.apply(element);

          builder.put(key, value);
        };
      }

      public BinaryOperator<ImmutableMap.Builder<K, V>> combiner() {
        return (builder1, builder2) -> {
          builder1.putAll(builder2.build());

          return builder1;
        };
      }

      public Function<ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> finisher() {
        return ImmutableMap.Builder<K, V>::build;
      }

      public Set<Collector.Characteristics> characteristics() {
        return ImmutableSet.of();
      }
    };
  }
}
