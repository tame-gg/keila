package gg.tame.palladium.command;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class PermissionedPalladiumSubcommand implements PalladiumSubcommand {

    private final Permission permission;

    protected PermissionedPalladiumSubcommand(Permission permission) {
        this.permission = permission;
    }

    protected PermissionedPalladiumSubcommand(String permission, PermissionDefault permissionDefault) {
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
