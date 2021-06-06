import { writable, Writable } from 'svelte/store';

export const state: Writable<any> = writable({});

export function renderProjectJson(st: any): string {
    const p = {
        project: st.project,
        compiler: {
            flags: {
                "tail-call-elimination": st.compilerFlags.tail,
                "fibonacci-elimination": st.compilerFlags.fib,
                "common-recursion-memoization": st.compilerFlags.memoization
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
    };
    return JSON.stringify(p);
}