import { writable, Writable } from 'svelte/store';

export const state: Writable<any> = writable({});

export function renderProjectJson(st: any): string {
    const p = {
        project: st.project,
        compiler: {
            flags: {
                "tail-call-elimination": st.compilerFlags.tail,
                "fibonacci-elimination": st.compilerFlags.fib,
                "common-recursion-memoization": st.compilerFlags.memoization,
                debug: true
            }
        },
        model: {
            "model-type": st.model.types.filter((v, i, a) => v.model)[0].name,
            "message-type": st.model.types.filter((v, i, a) => v.message)[0].name,
            types: st.model.types.map((v, i, a) => {
                const r: any = ({
                    base: v.baseType,
                    alias: v.name,
                    x: v.x,
                    y: v.y
                });
                if (v.variants) {
                    r.variants = v.variants.map((vv, ii, aa) => ({
                        name: vv.name,
                        type: vv.baseType
                    }));
                }
                return r;
            })
        },
        view: st.view,
        update: {
            nodes: st.update.nodes.map((v, i, a) => {
                const r: any = ({
                    type: v.type,
                    inputs: v.inputs,
                    output: v.output,
                    x: v.x,
                    y: v.y,
                });
                if (v.title) r.name = v.title;
                if (v.width) r.width = v.width;
                if (v.height) r.height = v.height;
                if (v.parent) r.parent = a.indexOf(v.parent);
                return r;
            }),
            relationships: st.update.relationships.map((v, i, a) => {
                const r: any = {
                    def: st.update.nodes.indexOf(v.source.node),
                    use: st.update.nodes.indexOf(v.destination.node),
                    'use-arg': v.destination.index,
                    'pass-by': v.source.passBy,
                    location: (v.source.position == 'internal' && v.source.type == 'input') || (v.destination.position == 'internal' && v.destination.type == 'output') || v.destination.node.type == 'model-output' ? 'internal' : 'external',
                };
                if (v.source.position == 'internal' && v.source.type == 'input') {
                    r.parameter = v.source.index;
                }

                return r;
            })
        }
    };
    return JSON.stringify(p);
}