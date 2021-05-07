<script lang="ts">
    import { makeDnD } from "../../common/dnd-util";

    export let name: string = "";
    export let baseType: string = "";

    export let x: number;
    export let y: number;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let model: boolean;
    export let setModel: () => void;

    export let deleteAction: () => void;
    export let setCurrentType: (t: any) => void;

    const setCurrentRefinedType = () =>
        setCurrentType({
            name: name,
            setName: (v) => (name = v),
            baseType: baseType,
            edit: false,
            deleteAction: deleteAction,
            setModel: setModel,
        });

    const { onMouseDown, onMouseUp, onMouseMove } = makeDnD((e) => {
        x += e.movementX;
        y += e.movementY;
    });

    function withCurrentType(handler: (e: MouseEvent) => void) {
        return (e: MouseEvent) => {
            setCurrentRefinedType();
            handler(e);
        };
    }
</script>

<svelte:window on:mouseup={onMouseUp} on:mousemove={onMouseMove} />

<div
    tabindex="0"
    on:mousedown={withCurrentType(onMouseDown)}
    class="fixed {model
        ? 'bg-blue-400'
        : 'bg-white'} shadow rounded cursor-move px-2 py-1 focus:ring-2 focus:ring-blue-600"
    style="left: {viewportOffsetX + x}px; top: {viewportOffsetY + y}px;"
>
    {name} <span class="mx-2 italic">of</span>
    <span class="font-medium">{baseType}</span>
</div>
