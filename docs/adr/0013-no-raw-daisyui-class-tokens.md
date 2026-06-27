# No raw DaisyUI class tokens in Mosaik-authored source

Mosaik-authored source, including documentation pages and docs infrastructure, should not directly author DaisyUI component class tokens such as `btn`, `card-body`, `loading-spinner`, `footer-title`, `input-bordered`, `table-zebra`, or `menu-active`. The docs must teach Mosaik as the public API, so DaisyUI tokens should be wrapped by components, structural sub-components, or type-safe modifiers; ordinary Tailwind utility classes remain acceptable pass-through styling.
