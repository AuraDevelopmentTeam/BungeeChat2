# Contribution Guidelines

We will always have a need for developers to help us improve this project. There is no such thing as a perfect project and things can always be improved. If you
are a developer and are interested in helping then please do not hesitate. Just make sure you follow our guidelines.

<!-- TOC depthFrom:2 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Cloning and Setting up](#cloning-and-setting-up)
	- [Cloning](#cloning)
		- [Adding Hooks](#adding-hooks)
	- [Setting up a Dev Env](#setting-up-a-dev-env)
- [Translating](#translating)
- [Code Style](#code-style)
	- [Adding Code](#adding-code)
	- [Adding Resources](#adding-resources)

<!-- /TOC -->

## Cloning and Setting up

### Cloning

Since this repository has a submodule you'll need to add the `--recursive` flag. So clone it like this:

```
mc_dev $ git clone --recursive https://github.com/AuraDevelopmentTeam/<project>.git
```

Should you have forgotten to to do that, don't worry you can easily fix it by running this command:

```
mc_dev/<project> $ git submodule update --init --recursive
```

#### Adding Hooks

I highly recommend adding some hooks to automatically checkout the right version of the submodule when you change versions, update the repo, etc.  
To do that juts run the provided gradle task: `./gradlew addGitHooks`.

All it does is copy the hooks from `GradleCommon/hooks` to `.git/hooks`.

### Setting up a Dev Env

Setting up a development environment works essentially like any other Forge project. So just run `./gradlew setupDecompWorkspace`.  
Or if you want it simpler, I set it up in a way that when you run either the `eclipse` or the `idea` IDE task, that `setupDecompWorkspace` gets run too.

*Note:  
This is only needed for projects that use Forge or SpongeCommon!*

## Translating

If you wish to add a translation or expand an existing one, I have another super useful gradle task for you! Just run `./gradlew checkTranslations`. This will
compare all translations to the base translation and make sure there aren't any extraneous or missing translation keys. So if you're adding the `ab_YZ`
translation and after running the task a `ab_YZ.lang.txt` file appears in the base directory, then you did something wrong. However this file will contain a
full report of the mismatched keys.

## Code Style

### Adding Code

We strictly follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).  
To make it easier for you we have provided the configuration files for Eclipse and IntelliJ in `/GradleCommon/formatter`. Also we have included a gradle task
that will format the code to the precise standard! So please run `./gradlew spotlessApply` before committing.

### Adding Resources

When adding PNG files make sure to save them with 24 or 32 bits and run `./gradlew optimizePng` (you'll need to have `optipng` available in your path though).  
Finally when you add JSOn files be sure to run `./gradlew formatJson` to ensure a clean formatting.  
