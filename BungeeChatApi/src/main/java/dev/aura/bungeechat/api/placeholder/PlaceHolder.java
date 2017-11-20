package dev.aura.bungeechat.api.placeholder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "placeholder")
public class PlaceHolder implements BungeeChatPlaceHolder {
    @Getter
    private final String placeholder;
    private final ReplacementSupplier replacementSupplier;
    private final List<Predicate<? super BungeeChatContext>> requirements = new LinkedList<>();

    @SafeVarargs
    public PlaceHolder(String placeholder, ReplacementSupplier replacementSupplier,
            Predicate<? super BungeeChatContext>... requirements) {
        this(placeholder, replacementSupplier, Arrays.asList(requirements));
    }

    public PlaceHolder(String placeholder, ReplacementSupplier replacementSupplier,
            List<Predicate<? super BungeeChatContext>> requirements) {
        if (placeholder.charAt(0) != '%') {
            placeholder = '%' + placeholder;
        }
        if (placeholder.charAt(placeholder.length() - 1) != '%') {
            placeholder = placeholder + '%';
        }

        this.placeholder = placeholder;
        this.replacementSupplier = replacementSupplier;
        this.requirements.addAll(requirements);
    }

    @Override
    public boolean isContextApplicable(BungeeChatContext context) {
        for (Predicate<? super BungeeChatContext> requirement : requirements) {
            if (!requirement.test(context))
                return false;
        }

        return true;
    }

    @Override
    public String apply(String message, BungeeChatContext context) {
        while (message.contains(placeholder)) {
            String replacement;

            try {
                replacement = replacementSupplier.get(context);
            } catch (RuntimeException e) {
                e.printStackTrace();

                replacement = "";
            }

            message = message.replace(placeholder, replacement);
        }

        return message;
    }

    public void addRequirement(Predicate<? super BungeeChatContext> requirement) {
        if (requirements.contains(requirement))
            return;

        requirements.add(requirement);
    }

    @Override
    public String getName() {
        return getPlaceholder();
    }

    public PlaceHolder[] createAliases(String... aliases) {
        int size = aliases.length;
        PlaceHolder[] placeHolders = new PlaceHolder[size + 1];

        for (int i = 0; i < size; i++) {
            placeHolders[i] = new PlaceHolder(aliases[i], replacementSupplier, requirements);
        }

        placeHolders[size] = this;

        return placeHolders;
    }
}
