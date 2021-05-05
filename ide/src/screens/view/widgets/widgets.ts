import TextBox from './TextBox.svelte';
import Button from './Button.svelte';
import Label from './Label.svelte';

import TextBoxIcon from '../../../icons/widgets/TextBox.svelte';
import ButtonIcon from '../../../icons/widgets/Button.svelte';
import LabelIcon from '../../../icons/widgets/Label.svelte';

export const widgetList = [
    { title: 'TextBox', icon: TextBoxIcon },
    { title: 'Button', icon: ButtonIcon },
    { title: 'Label', icon: LabelIcon },
];

export const widgetsByName = {
    'TextBox': TextBox,
    'Button': Button,
    'Label': Label
};