<script lang="ts">
    export let width: number = 800;
    export let height: number = 600;

    export let viewportOffsetX: number = 0;
    export let viewportOffsetY: number = 0;

    export let addWidget: (name: string, e: DragEvent) => void;

    let isDragHovered = false;

    function onDrop(e: DragEvent) {
        isDragHovered = false;
        addWidget(e.dataTransfer.getData("widgetName"), e);
    }
</script>

<div
    on:dragenter={(_) => (isDragHovered = true)}
    on:dragleave={(_) => (isDragHovered = false)}
    on:drop={onDrop}
    on:dragover={(e) => e.preventDefault()}
    class="fixed {isDragHovered ? 'bg-gray-50' : 'bg-white'} shadow-lg"
    style="left: {viewportOffsetX}px; top: {viewportOffsetY}px; width: {width}px; height: {height}px"
/>
