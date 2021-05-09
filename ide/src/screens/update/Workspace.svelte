<script lang="ts">
    import Endpoint from "./objects/Endpoint.svelte";
    import FunctionCall from "./objects/FunctionCall.svelte";
    import FunctionDefinition from "./objects/FunctionDefinition.svelte";

    export let resizeTarget;
</script>

<div
    class="fixed w-screen h-screen bg-gray-100"
    on:mousedown={() => (resizeTarget = null)}
/>

<Endpoint x={500} y={200} title="Model" />
<Endpoint x={480} y={600} title="Message" />
<Endpoint x={1600} y={400} title="View" titleAlignment="right" />

<FunctionCall x={500} y={100} title="String.toInt" />
<FunctionDefinition
    bind:target={resizeTarget}
    x={700}
    y={100}
/>
