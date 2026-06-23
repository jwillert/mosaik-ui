# Reference: ShadCN UI Pattern

## What Mosaik borrows from ShadCN

- **Copy-paste philosophy:** components are owned by the user after installation, not a dependency
- **Registry-based:** central manifest of available components with metadata
- **CLI-driven install:** single command to add a component (ShadCN: `npx shadcn add button`, Mosaik: `./gradlew mosaikAdd --component=button`)
- **Dependency resolution:** components can depend on other components, auto-installed
- **Documentation site:** built with the components themselves (dogfooding)
- **Customizable:** user modifies components after installation to fit their needs

## Where Mosaik differs

| Aspect | ShadCN | Mosaik |
|--------|--------|--------|
| Runtime | React (client-side) | kotlinx.html (server-side) |
| Styling | Tailwind + custom CSS variables | Tailwind + DaisyUI |
| Distribution | npm package + remote registry | Gradle Plugin + bundled registry |
| CLI | Standalone (`npx shadcn`) | Gradle tasks (`./gradlew mosaikAdd`) |
| Component format | `.tsx` files | `.kt` files |
| Theming | CSS variables | DaisyUI themes (data-theme attribute) |
| Interactivity | React state + client JS | CSS-only for v1 |

## Key ShadCN design choices worth studying

1. **components.json** — project-level config (like our `mosaikUi {}` extension)
2. **One file per component** — atomic installs
3. **utils.ts** — shared utility file (like our Theme.kt)
4. **No abstraction over the underlying library** — Radix primitives are used directly, not wrapped. Similarly, we should not abstract over DaisyUI — use its classes directly.
