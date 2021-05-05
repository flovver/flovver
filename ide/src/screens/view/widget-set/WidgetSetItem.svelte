<script lang="ts">
    export let title: string;

    function onDragStart(e: DragEvent) {
        e.dataTransfer.setData('widgetName', title);
    }
</script>

<div
    on:dragstart={onDragStart}
    draggable="true"
    class="flex m-2 px-2 py-1 items-center cursor-move hover:ring ring-black rounded shadow bg-gray-50"
>
    <div class="inline-block mr-4">
        <slot name="icon" />
    </div>
    <span class="inline-block">{title}</span>
</div>
