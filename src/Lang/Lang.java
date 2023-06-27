package Lang;

public class Lang
{
    public enum Language
    {
        Romanian
    }
    public static String EmailTaken,SuccessSaveData,ErrorSaveData,ErrorChangeChapterName,
            SuccessEditData,GenericError,KeyAlreadyExists,AddKeyWordPopUp,AddChapterFrameTitle,NameChapterLabel,Cancel,Add,
            EmptyChapterNameField,ChapterAlreadyExists,SuccessAddChapter,AddKeyWordTitle,KeyWordName,Information,Back,KeyWordEmpty,KeyWordInfoEmpty,
            AddLessonTitle,LessonNameLabel,LessonLabel,AddLessonLabel,EmptyLessonNameField,EmptyLessonField,LessonAlreadyExists,SuccessAddLesson,
            TestLabel,AddTestTitle,TestOptionLabel,PointsLabel,TotalPointsLabel,AddTestLabel,AddAnotherSubject,OnlyDigits,PointsLimit,EmptySubjectField,
            EmptyOptionField,SuccessAddTest,UserNameLabel,PasswordLabel,NameLabel,LastNameLabel,EmailLabel,AgeLabel,EmptyUserNameField,UserNameContainSpace,
            UserNameMinChar,EmptyPasswordField,PasswordMinMaxChar,EmptyLastNameField,LastNameInvalidChar,LastNameMinChar,EmptyNameField,NameInvalidChar,
            NameMinChar,EmptyEmailField,EmailInvalid,EmptyAgeField,AgeInvalid,MinAgeField,OverAgeField,UserNameAlreadyExists,SuccessAddUser,ManagerUsersTitle,
            AddStudentLabel,PrevButton,NextButton,UserDoesntExist,ManagerChaptersTitle,AddChapterField,ChaptersTitle,ChapterAdminButton,ChapterUserButton,
            Save,EditSubjectTitle,SaveSubjectLabel,TotalPoints_SubjLabel,DeleteSubjectLabel,EmptyTestDeleted,SuccessDeletingSubject,
            SuccessEditSubject,EditUserTitle,EditPasswordLabel,DeleteUserLabel,SuccessDeletingUser,SuccessEditUser,ResultTestLabel,NotAttemptTest,AddKeyLabel,
            AddTestLabel_Two,DeleteTestLabel,SuccessDeletingTest,LessonAdminLabel,LessonAdminHasTest,AddLessonLabel_Two,EditChapterLabel,LessonUserLabel,
            LessonUserHasTest_One,LessonUserHasTest_Two,LessonUserRead,ChapterResultTest,FinalExam,FinalExamRequest,StartTestLabel,LessonTestRequest,
            MarkReadLessonLabel,PrintLessonLabel,LoginTitle,LoginLabel,WelcomeLabel,ManageProfileAdminLabel,ManageUsersLabel,ManageChaptersLabel,
            ManageSubjectsLabel,DashboardTitle,ManageProfileUserLabel,ChaptersLabel,ProfileTitle,ProfileRoleAdminLabel,ProfileRoleUserLabel,ProfileUserNameLabel,
            ManagerSubjectTitle,SearchLabel,SubjectLabel,TestTitle,TestOptionContentLabel,CurrentSubjectLabel,NextSubjectLabel,SendTestLabel,ExitLabel,
            TestPointsMessage,LoginFailMessage,AddUserTitle,CloseTabsMessage;
    public static void setLang(Language language)
    {
        // TO DO: read from file instead of the class
        if(language == Language.Romanian)
            Romanian.changeLang();
    }

}
