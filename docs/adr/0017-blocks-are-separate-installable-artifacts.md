# Blocks are separate installable artifacts

Mosaik will model Blocks separately from Components. Components are DaisyUI Reference Components that wrap one DaisyUI component family; Blocks are composed examples such as dashboards or data tables that depend on Components and may include sample data, routes, and documented htmx/Alpine.js/Datastar attributes. Keeping Blocks out of the component registry prevents patterns like data tables from becoming fake primitives while still preserving the shadcn-style source-install model for larger examples.
