# ADR 0010: Type-safe DaisyUI Modifiers

Mosaik will expose DaisyUI component modifiers as type-safe Kotlin parameters/enums instead of documenting raw DaisyUI class strings as the normal API. The generic `classes` parameter remains as an escape hatch for layout and one-off styling utilities, but component semantics such as button outline/soft/block/circle or footer center should be represented in Mosaik's API before being promoted in the docs. This keeps the DaisyUI implementation mostly invisible while preserving raw kotlinx.html receivers for attributes and third-party extensions.
