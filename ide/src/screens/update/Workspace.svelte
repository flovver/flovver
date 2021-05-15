<script lang="ts">
    import Endpoint from "./nodes/Endpoint.svelte";
    import FunctionCall from "./nodes/FunctionCall.svelte";
    import FunctionDefinition from "./nodes/FunctionDefinition.svelte";

    export let resizeTarget;
</script>

<div
    class="fixed w-screen h-screen bg-gray-100"
    on:mousedown={() => (resizeTarget = null)}
/>

<Endpoint x={500} y={200} title="Model" />
<Endpoint x={480} y={600} title="Message" />
<Endpoint x={1600} y={400} title="View" titleAlignment="right" />

<FunctionDefinition bind:target={resizeTarget} x={700} y={100} inputs={["Int", "Int"]} />

<FunctionCall x={500} y={100} title="self" inputs={["Int", "Int"]} />
<FunctionCall x={500} y={100} title="<" inputs={["Int", "Int"]} />
<FunctionCall x={500} y={100} title="2" />
<FunctionCall x={500} y={100} title="if" inputs={["Boolean", "a -> b", "a -> b"]} />
<FunctionCall x={500} y={100} title="-" inputs={["Int", "Int"]} />
<FunctionCall x={500} y={100} title="1" />
<FunctionCall x={500} y={100} title="*" inputs={["Int", "Int"]} />
<FunctionCall x={500} y={100} title="I" inputs={["a"]} />
<FunctionCall x={500} y={100} title="case" inputs={[1, 2, 3]} />
<FunctionCall x={500} y={100} title="String.toInt"/>
<Endpoint x={500} y={200} title="NewInput" />
<Endpoint x={500} y={200} title="ComputeFactorial" />
