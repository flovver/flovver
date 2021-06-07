<script lang="ts">
    import Line from "./connections/Line.svelte";
    import { connectionMessages } from "./stores";
    import { state } from "../common/global-state";

    let connections = [];

    $: {
        state.update((v) => {
            if (!v.update) {
                v.update = {};
            }
            if (!v.update.relationships) {
                v.update.relationships = {};
            }
            v.update.relationships = connections;
            return v;
        });
    }

    let source = null;

    function setSource(s) {
        source = s;

        return (x, y) => {
            s.x = x;
            s.y = y;

            connections = connections;
        };
    }

    function addConnection(d) {
        if (source == null) return null;

        const c = { source: source, destination: d };

        connections = connections.filter((v, i, a) => v.destination != d);
        connections.push(c);
        source = null;
        connections = connections;

        return [
            (x, y) => {
                d.x = x;
                d.y = y;

                connections = connections;
            },
            () => (connections = connections.filter((v, i, arr) => v != c)),
        ];
    }

    connectionMessages.set({
        setSource: setSource,
        addConnection: addConnection,
    });
</script>

{#each connections as c}
    <Line
        x1={c.source.x + 3}
        y1={c.source.y + 3}
        x2={c.destination.x + 3}
        y2={c.destination.y + 3}
    />
{/each}
