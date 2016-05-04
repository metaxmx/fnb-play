package models

case class Permission(_id: String,
                      permissionType: String,
                      permission: String,
                      accessRule: AccessRule) extends BaseModel[Permission] {

  override def withId(_id: String) = copy(_id = _id)

}
