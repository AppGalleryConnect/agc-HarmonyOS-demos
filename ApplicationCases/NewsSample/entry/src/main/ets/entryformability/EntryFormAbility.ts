import formInfo from '@ohos.app.form.formInfo';
import formBindingData from '@ohos.app.form.formBindingData';
import FormExtensionAbility from '@ohos.app.form.FormExtensionAbility';
import NewsViewModel from '../viewmodel/NewsViewModel';
import formProvider from '@ohos.app.form.formProvider';

export default class EntryFormAbility extends FormExtensionAbility {
  onAddForm(want) {
    // Called to return a FormBindingData object.
    let news=NewsViewModel.getRandomNews();
    let formData ={
      "id":news.id,
      "title":news.title,
      "image":news.imagesUrl[0].url
    }
    console.error("newsInfo:"+JSON.stringify(formData))
    return formBindingData.createFormBindingData(formData);
  }

  onCastToNormalForm(formId) {
    // Called when the form provider is notified that a temporary form is successfully
    // converted to a normal form.
  }

  onUpdateForm(formId) {
    // Called to notify the form provider to update a specified form.
  }

  onChangeFormVisibility(newStatus) {
    // Called when the form provider receives form events from the system.
  }

  onFormEvent(formId, message) {
    let news=NewsViewModel.getRandomNews();
    let formData ={
      "id":news.id,
      "title":news.title,
      "image":news.imagesUrl[0].url
    }

    let updateData = formBindingData.createFormBindingData(formData)
    formProvider.updateForm(formId, updateData).catch((err) => {
      console.error(`processNewsData newsInfo, err: ${JSON.stringify(err)}`);
    });
  }

  onRemoveForm(formId) {
    // Called to notify the form provider that a specified form has been destroyed.
  }

  onAcquireFormState(want) {
    // Called to return a {@link FormState} object.
    return formInfo.FormState.READY;
  }
};