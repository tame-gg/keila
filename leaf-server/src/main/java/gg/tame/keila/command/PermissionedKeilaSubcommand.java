package gg.tame.keila.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class PermissionedKeilaSubcommand implements KeilaSubcommand {

    private final Permission permission;

    protected PermissionedKeilaSubcommand(Permission permission) {
        this.permission = permission;
    }

    protected PermissionedKeilaSubcommand(String permission, PermissionDefault permissionDefault) {
        this(new Permission(permission, permissionDefault));
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }

    @Override
    public Permission getPermission() {
        return this.permission;
    }
}
