package dev.aura.bungeechat.api.placeholder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import lombok.Getter;

public class Placeholder {
	@Getter
	private final String placeholder;
	private final ReplacementSupplier replacementSupplier;
	private final List<Predicate<? super BungeeChatContext>> requirements = new LinkedList<>();

	@SafeVarargs
	public Placeholder(String placeholder, ReplacementSupplier replacementSupplier,
			Predicate<? super BungeeChatContext>... requirements) {
		if (placeholder.charAt(0) != '%') {
			placeholder = '%' + placeholder;
		}
		if (placeholder.charAt(placeholder.length() - 1) != '%') {
			placeholder = placeholder + '%';
		}

		this.placeholder = placeholder;
		this.replacementSupplier = replacementSupplier;
		this.requirements.addAll(Arrays.asList(requirements));
	}

	public boolean isContextApplicable(BungeeChatContext context) {
		for (Predicate<? super BungeeChatContext> requirement : requirements) {
			if (requirement.test(context))
				return false;
		}

		return true;
	}

	public String apply(String message, BungeeChatContext context) {
		while (message.contains(placeholder)) {
			message = message.replace(placeholder, replacementSupplier.get(context));
		}

		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Placeholder))
			return false;

		return placeholder.equals(((Placeholder) obj).placeholder);
	}

	public void addRequirement(Predicate<? super BungeeChatContext> requirement) {
		if (requirements.contains(requirement))
			return;

		requirements.add(requirement);
	}
}
