<script lang="ts">
    import SideBar from '../layout/SideBar.svelte';

    import LabelIcon from '../icons/widgets/Label.svelte';
    import TextBoxIcon from '../icons/widgets/TextBox.svelte';
    import ButtonIcon from '../icons/widgets/Button.svelte';

    import WidgetSet from './view/widget-set/WidgetSet.svelte';
    import WidgetSetItem from './view/widget-set/WidgetSetItem.svelte';
    import Workspace from './view/Workspace.svelte';
</script>

<Workspace/>

<SideBar title="Widgets" position="left">

    <WidgetSet>

        <WidgetSetItem title="Label">
            <LabelIcon slot="icon"/>
        </WidgetSetItem>

        <WidgetSetItem title="TextBox">
            <TextBoxIcon slot="icon"/>
        </WidgetSetItem>

        <WidgetSetItem title="Button">
            <ButtonIcon slot="icon"/>
        </WidgetSetItem>

    </WidgetSet>

</SideBar>

<SideBar title="Preferences" position="right">
</SideBar>