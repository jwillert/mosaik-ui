# Drawer owns its toggle contract

Drawer is a compound component whose DaisyUI structure only works when the hidden toggle checkbox, content, and side panel are siblings under the drawer root. Mosaik will render the checkbox from `mDrawer(id = ...)`, expose scoped slots for content and side, and provide a label-based `mDrawerButton` that reuses the existing button modifiers; this makes `drawer` depend on `button`, but keeps the visible trigger type-safe and prevents consumers from assembling invalid drawer markup.
