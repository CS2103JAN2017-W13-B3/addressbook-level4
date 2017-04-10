# About Us

We are a team based in the [School of Computing, National University of Singapore](http://www.comp.nus.edu.sg).

## Project Team

#### [Louis Lai](http://github.com/louislai)
<img src="images/louislai.png" width="150"><br>
Role: Architect <br>

* Components in charge of: UI, Architecture, Storage
* Aspects/tools in charge of: Scheduling and tracking, CI, Code quality
* Features implemented:
  - [Mark complete/incomplete single/multiple tasks](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#39-marking-a-task-as-complete-or-incomplete-mark)
  - [Undo single/multiple times](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#313-undoing-a-command-undo)
  - [Redo single/multiple times](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#314-redoing-a-command-redo)
  - [Alias command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#316-adding-an-alias-for-any-phrase-alias)
  - [Unalias command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#317-removing-an-alias-unalias)
  - [Viewalias command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#318-viewing-aliases-viewalias)
  - [Save command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#320-specifying-a-new-storage-location-to-save-data-to-save)
  - [Load command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#321-specifying-a-new-storage-location-to-load-data-from-load)
  - [Most of the GUI including the different tab filter](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#23-get-used-to-the-interface) + [Switch command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#33-switching-to-a-different-tab-switch)
  - [Refactor Controller classes to support smart command suggestions (command word suggestion, parameter suggestion, parameter options suggestion, search keyword suggestion)](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#32-getting-keyword-suggestions-smartly)
* Code written:
  - [Functional Codes](../collated/main/A0131125Y.md)
  - [Test Codes](../collated/test/A0131125Y.md)
  - [Documentation](../collated/docs/A0131125Y.md)
* Other major contributions:
  - [Designed the MVC architecture for back-end](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/DeveloperGuide.md#3-design)
  - [Designed the reactive architecture for front-end](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/DeveloperGuide.md#333-reactive-nature-of-the-ui)
  - Set up CI for the project
  - Set up GUI test infrastructure for the project
  - Do code review for every PR

-----

#### [Melvin Tan](http://github.com/Melvin-Tan)
<img src="images/melvin-tan.png" width="150"><br>
Role: Developer <br>

* Components in charge of: Controller, Model, Tokenizer
* Aspects/tools in charge of: Testing, Eclipse, Natty
* Features implemented:
    - [Add and update for all types of tasks (including floating task, task with deadline, event, recurring task)](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#34-adding-a-task-add)
    - [Delete single/multiple tasks](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#38-deleting-a-task-delete)
* Code written:
    - [Functional Codes](../collated/main/A0127545A.md)
    - [Test Codes](../collated/test/A0127545A.md)
    - [Documentation](../collated/docs/A0127545A.md)
* Other major contributions:
    - [Wrote most of the re-usable Tokenizer/Parser classes to be used by various Controllers](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/DeveloperGuide.md#353-controller-common-classes)
    - Put in validation for most of the model
    - Heavily involved in peer-reviewing most of the PR


-----

#### [Lewis Koh](http://github.com/Rinder5)
<img src="images/rinder5.png" width="150"><br>
Role: Developer <br>

* Components in charge of: Controller, Model
* Aspects/tools in charge of: Documentation
* Feature implemented:
  - [Tag command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#310-adding-a-tag-to-a-task-tag)
  - [Untag command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#311-removing-a-tag-from-a-task-untag)
  - [Find/Filter/List command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#36-filtering-all-tasks-for-a-given-keyword-filter)
  - [Clear command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#312-clearing-all-entries-clear)
  - [History command + Hot key up & down to navigate command history](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#315-viewing-previous-commands-history)
  - [Sort command](https://github.com/CS2103JAN2017-W13-B3/main/blob/master/docs/UserGuide.md#37-sorting-all-tasks-in-a-given-order-sort)
* Code written:
  - [Functional Codes](../collated/main/A0162011A.md)
  - [Test Codes](../collated/test/A0162011A.md)
  - [Documentation](../collated/docs/A0162011A.md)
* Other major contribution:
  - Regularly update the project manual
  - Add help descriptions to all commands
